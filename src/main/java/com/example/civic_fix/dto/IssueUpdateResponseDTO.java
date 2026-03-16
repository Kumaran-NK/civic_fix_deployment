package com.example.civic_fix.dto;

import java.time.LocalDateTime;
import com.example.civic_fix.Entity.IssueStatus;
import lombok.Data;

@Data
public class IssueUpdateResponseDTO {
    private Long updateId;
    private Long issueId;
    private String issueTitle;
    private Long updatedById;
    private String updatedByName;
    private String message;
    private IssueStatus statusAfterUpdate; 
    private String statusDisplayName;  
    private LocalDateTime createdAt;
}