package com.example.civic_fix.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private Long notificationId;
    private Long userId;
    private String userName;
    private Long issueId;
    private String issueTitle;
    private String type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}