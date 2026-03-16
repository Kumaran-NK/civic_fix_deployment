package com.example.civic_fix.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BroadcastResponseDTO {
    private Long notificationId;
    private String type;
    private String message;
    private int sentTo;
    private LocalDateTime sentAt;
}