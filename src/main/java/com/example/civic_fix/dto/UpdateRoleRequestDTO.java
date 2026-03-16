package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class UpdateRoleRequestDTO {
    private String role;
    private Long departmentId; 
}