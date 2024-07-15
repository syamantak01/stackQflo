package com.dexcode.stackQflo.controllers;

import com.dexcode.stackQflo.dto.JwtAuthRequestDTO;
import com.dexcode.stackQflo.dto.JwtAuthResponseDTO;
import com.dexcode.stackQflo.security.JwtService;
import com.dexcode.stackQflo.services.AuthService;
import com.dexcode.stackQflo.services.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticate(
            @RequestBody JwtAuthRequestDTO request
            ){

        // authenticate user
        authService.authenticate(request);

        //after successful authentication, we need to generate token
        UserDetails userDetail = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetail);

        // Create Response with token
        JwtAuthResponseDTO response = new JwtAuthResponseDTO();
        response.setToken(token);

        return ResponseEntity.ok(response);

    }

}
