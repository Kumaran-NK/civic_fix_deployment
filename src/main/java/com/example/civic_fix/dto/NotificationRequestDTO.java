package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private Long userId;
    private Long issueId;
    private String type;
    private String message;
}