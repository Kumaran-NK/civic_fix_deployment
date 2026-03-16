package com.example.civic_fix.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.Notification;
import com.example.civic_fix.Entity.Role;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.Repository.NotificationRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.BroadcastResponseDTO;  // FIXED: Correct spelling
import com.example.civic_fix.dto.BroadcastNotificationRequest;
import com.example.civic_fix.dto.NotificationRequestDTO;
import com.example.civic_fix.dto.NotificationResponseDTO;
import com.example.civic_fix.mapper.DTOMapper;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final DTOMapper dtoMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                  UserRepository userRepository,
                                  IssueRepository issueRepository,
                                  DTOMapper dtoMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Issue issue = null;
        if (request.getIssueId() != null) {
            issue = issueRepository.findById(request.getIssueId())
                    .orElseThrow(() -> new RuntimeException("Issue not found"));
        }

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setIssue(issue);
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return dtoMapper.toNotificationDTO(savedNotification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserId(userId);
        return dtoMapper.toNotificationDTOList(notifications);
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserIdAndIsReadFalse(userId);
        return dtoMapper.toNotificationDTOList(notifications);
    }

    @Override
    public NotificationResponseDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return dtoMapper.toNotificationDTO(updatedNotification);
    }

    @Override
    public BroadcastResponseDTO broadcastNotification(BroadcastNotificationRequest request) {  // FIXED: Return type
        List<User> targetUsers;
        
        if (request.getTargetRoles() == null || request.getTargetRoles().isEmpty()) {
            // Send to all users
            targetUsers = userRepository.findAll();
        } else {
            // Send to specific roles
            targetUsers = new ArrayList<>();
            for (String roleName : request.getTargetRoles()) {
                try {
                    Role role = Role.valueOf(roleName.toUpperCase());
                    targetUsers.addAll(userRepository.findByRole(role));
                } catch (IllegalArgumentException e) {
                    // Skip invalid role
                }
            }
        }
        
        int sentCount = 0;
        Notification sampleNotification = null;
        
        for (User user : targetUsers) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType(request.getType());
            notification.setMessage(request.getMessage());
            notification.setIsRead(false);
            
            Notification saved = notificationRepository.save(notification);
            if (sampleNotification == null) {
                sampleNotification = saved;
            }
            sentCount++;
        }
        
        BroadcastResponseDTO response = new BroadcastResponseDTO();  // FIXED: Correct class name
        if (sampleNotification != null) {
            response.setNotificationId(sampleNotification.getNotificationId());
        }
        response.setType(request.getType());
        response.setMessage(request.getMessage());
        response.setSentTo(sentCount);
        response.setSentAt(LocalDateTime.now());
        
        return response;
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return dtoMapper.toNotificationDTOList(notifications);
    }
}