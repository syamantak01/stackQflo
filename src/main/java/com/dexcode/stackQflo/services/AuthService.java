package com.dexcode.stackQflo.services;

import com.dexcode.stackQflo.dto.JwtAuthRequestDTO;

public interface AuthService {

    void authenticate(JwtAuthRequestDTO jwtAuthRequestDTO);
}
