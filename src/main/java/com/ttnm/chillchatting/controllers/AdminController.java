package com.ttnm.chillchatting.controllers;

import com.ttnm.chillchatting.configs.jwt.JwtTokenProvider;
import com.ttnm.chillchatting.dtos.LoginResponse;
import com.ttnm.chillchatting.dtos.badword.BadWordDto;
import com.ttnm.chillchatting.dtos.statistic.Statistic;
import com.ttnm.chillchatting.dtos.user.NewPassDto;
import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.BadWord;
import com.ttnm.chillchatting.entities.User;
import com.ttnm.chillchatting.services.badword.BadWordService;
import com.ttnm.chillchatting.services.message.MessageService;
import com.ttnm.chillchatting.services.user.UserService;
import com.ttnm.chillchatting.utils.CustomUserDetails;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/admin")
public class AdminController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    private final MessageService messageService;

    private final BadWordService badWordService;

    private final SimpMessagingTemplate template;

    private final SimpUserRegistry simpUserRegistry;

    public AdminController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService, MessageService messageService, BadWordService badWordService, SimpMessagingTemplate template, SimpUserRegistry simpUserRegistry) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.messageService = messageService;
        this.badWordService = badWordService;
        this.template = template;
        this.simpUserRegistry = simpUserRegistry;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody UserDto loginRequest) {

        // X??c th???c t??? username v?? password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        // N???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
        // Set th??ng tin authentication v??o Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tr??? v??? jwt cho ng?????i d??ng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        System.out.println(loginRequest.getUserName() + " " + loginRequest.getPassword());
        return new ResponseEntity<>(new LoginResponse(jwt), HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<User> registNewAdmin(@RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.createNewAdmin(dto), HttpStatus.OK);
    }


    @GetMapping("/statistic")
    public ResponseEntity<Statistic> getStatistic() {
        return new ResponseEntity<>(messageService.getStatistic(), HttpStatus.OK);
    }

    @GetMapping("/bad-word")
    public ResponseEntity<List<BadWord>> getAllBadWord() {
        return new ResponseEntity<>(badWordService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/bad-word")
    public ResponseEntity<BadWord> createBadWord(@RequestBody BadWordDto dto) {
        BadWord badWord = badWordService.createNewBadWord(dto);
        messageService.updateFilterForLatestMessage();
        return new ResponseEntity<>(badWord, HttpStatus.OK);
    }

    @PutMapping("/bad-word/{id}")
    public ResponseEntity<BadWord> updateBadWord(@PathVariable String id, @RequestBody BadWordDto dto) {
        BadWord badWord = badWordService.updateBadWord(id, dto);
        messageService.updateFilterForLatestMessage();
        return new ResponseEntity<>(badWord, HttpStatus.OK);
    }

    @DeleteMapping("/bad-word/{id}")
    public ResponseEntity<String> deleteBadWord(@PathVariable String id) {
        badWordService.deleteBadWord(id);
        messageService.updateFilterForLatestMessage();
        return new ResponseEntity<>("Delete bad word filter success", HttpStatus.OK);
    }

    @GetMapping("/number-user")
    public ResponseEntity<Integer> getNumberOfUser() {
        return new ResponseEntity<>(simpUserRegistry.getUserCount(), HttpStatus.OK);
    }

    @EventListener(SessionConnectedEvent.class)
    public void handleWebsocketConnectListner(SessionConnectedEvent event) {
        this.template.convertAndSend("/message/number-user", simpUserRegistry.getUserCount());
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
        this.template.convertAndSend("/message/number-user", simpUserRegistry.getUserCount());
    }

    @GetMapping("/list-admin")
    public ResponseEntity<List<User>> getListUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @PostMapping("/change-pass")
    public ResponseEntity<User> changePass(@RequestBody NewPassDto newPassDto, HttpServletRequest request) {
        return new ResponseEntity<>(userService.changePass(newPassDto, request), HttpStatus.OK);
    }
}
