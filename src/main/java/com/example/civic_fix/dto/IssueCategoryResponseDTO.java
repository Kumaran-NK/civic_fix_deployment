package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class IssueCategoryResponseDTO {
    private Long categoryId;
    private String name;
    private Integer baseScore;
    private DepartmentResponseDTO department;
}