package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private Long departmentId;
    private String zone;
}