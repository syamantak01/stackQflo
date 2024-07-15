package com.dexcode.stackQflo.dto;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String userName;
    private String password;
}
