package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class BulkAssignItemResult {
    private Long issueId;
    private String status;
    private String message;
}
