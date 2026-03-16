package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class DepartmentResponseDTO {
    private Long departmentId;
    private String departmentName;
    private String description;
}