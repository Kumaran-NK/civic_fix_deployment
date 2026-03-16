package com.example.civic_fix.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private String entityType;
    private Long entityId;
    private Long userId;
    private String username;
    private String details;
    private LocalDateTime timestamp;
    private String ipAddress;
}


