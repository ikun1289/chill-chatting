package com.ttnm.chillchatting.controllers;

import com.ttnm.chillchatting.configs.jwt.JwtTokenProvider;
import com.ttnm.chillchatting.dtos.LoginResponse;
import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.utils.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/admin")
public class AdminController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    public AdminController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody UserDto loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        System.out.println(loginRequest.getUserName() + " " + loginRequest.getPassword());
        return new LoginResponse(jwt);
    }
}
