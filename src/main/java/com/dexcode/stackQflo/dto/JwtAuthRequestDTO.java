package com.dexcode.stackQflo.dto;

import lombok.Data;

@Data
public class JwtAuthRequestDTO {
    private String username;
    private String password;
}
