package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class IssueCategoryRequestDTO {
    private String name;
    private Integer baseScore;
    private Long departmentId;
}