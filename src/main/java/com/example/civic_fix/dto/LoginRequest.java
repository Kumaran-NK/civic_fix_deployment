package com.example.civic_fix.dto;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}