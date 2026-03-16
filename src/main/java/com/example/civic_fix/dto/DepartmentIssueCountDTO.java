package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class DepartmentIssueCountDTO {
    private Long departmentId;
    private String departmentName;
    private long issueCount;
}