package com.example.civic_fix.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResolveIssueResponse {
    private Long issueId;
    private String title;
    private String status;
    private String resolutionNotes;
    private LocalDateTime resolvedAt;
    private String resolvedBy;
}