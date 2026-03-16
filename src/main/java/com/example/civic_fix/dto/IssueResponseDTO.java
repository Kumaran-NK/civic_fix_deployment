package com.example.civic_fix.dto;

import java.time.LocalDateTime;

import com.example.civic_fix.Entity.IssueStatus;

import lombok.Data;

@Data
public class IssueResponseDTO {
    private Long issueId;
    private String title;
    private String description;
    private IssueStatus status;
    private String statusDisplayName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    
    private Long reportedById;
    private String reportedByName;
    private String reportedByEmail;
    
 
    private Long categoryId;
    private String categoryName;
    
    
    private Long assignedDepartmentId;
    private String assignedDepartmentName;

    private Long assignedOfficerId;
    private String assignedOfficerName;
  
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}