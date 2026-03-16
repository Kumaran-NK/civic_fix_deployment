package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class ReassignIssueRequest {
    private Long departmentId;
    private Long officerId;
    private String reason;
}