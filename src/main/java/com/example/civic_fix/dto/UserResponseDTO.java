package com.example.civic_fix.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private Long departmentId;
    private String departmentName;
    private String zone;
    private LocalDateTime createdAt;
}