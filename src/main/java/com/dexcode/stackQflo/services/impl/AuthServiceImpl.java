package com.dexcode.stackQflo.services.impl;

import com.dexcode.stackQflo.dto.JwtAuthRequestDTO;
import com.dexcode.stackQflo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void authenticate(JwtAuthRequestDTO jwtAuthRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtAuthRequestDTO.getUsername(),
                        jwtAuthRequestDTO.getPassword()
                )
        );
    }
}
