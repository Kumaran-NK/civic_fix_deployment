package com.example.civic_fix.dto;

import com.example.civic_fix.Entity.IssueStatus;
import lombok.Data;

@Data
public class IssueUpdateRequestDTO {
    private Long issueId;
    private String message;
    private IssueStatus statusAfterUpdate;  
}