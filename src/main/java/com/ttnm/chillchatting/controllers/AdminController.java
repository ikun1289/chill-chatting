package com.ttnm.chillchatting.controllers;

import com.ttnm.chillchatting.configs.jwt.JwtTokenProvider;
import com.ttnm.chillchatting.dtos.LoginResponse;
import com.ttnm.chillchatting.dtos.statistic.Statistic;
import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.entities.User;
import com.ttnm.chillchatting.services.message.MessageService;
import com.ttnm.chillchatting.services.user.UserService;
import com.ttnm.chillchatting.utils.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin")
public class AdminController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    private final MessageService messageService;

    public AdminController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService, MessageService messageService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.messageService = messageService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody UserDto loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
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
}
