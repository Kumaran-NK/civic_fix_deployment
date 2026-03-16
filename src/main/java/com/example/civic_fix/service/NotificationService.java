package com.example.civic_fix.service;

import java.util.List;

import com.example.civic_fix.dto.BroadcastNotificationRequest;
import com.example.civic_fix.dto.BroadcastResponseDTO;
import com.example.civic_fix.dto.NotificationRequestDTO;
import com.example.civic_fix.dto.NotificationResponseDTO;

public interface NotificationService {

    NotificationResponseDTO createNotification(NotificationRequestDTO request);
    List<NotificationResponseDTO> getNotificationsByUser(Long userId);
    List<NotificationResponseDTO> getUnreadNotifications(Long userId);
    NotificationResponseDTO markAsRead(Long notificationId);
    BroadcastResponseDTO broadcastNotification(BroadcastNotificationRequest request);
    List<NotificationResponseDTO> getAllNotifications(); 
}