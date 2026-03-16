package com.example.civic_fix.dto;

import lombok.Data;
import java.util.List;

@Data
public class BroadcastNotificationRequest{
    private String type;
    private String message;
    private List<String> targetRoles;
}