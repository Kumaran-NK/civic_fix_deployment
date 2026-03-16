package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class AssignIssueRequest {

    private Long issueId;
    private Long departmentId;
    private Long officerId;
}
