package com.opencode.practice.controllers;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String email;
    private String password;
}
