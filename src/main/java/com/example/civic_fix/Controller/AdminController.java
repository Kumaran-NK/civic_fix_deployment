package com.example.civic_fix.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.civic_fix.dto.AuditLogPageDTO;
import com.example.civic_fix.dto.BroadcastNotificationRequest;
import com.example.civic_fix.dto.BroadcastResponseDTO;
import com.example.civic_fix.dto.NotificationResponseDTO;
import com.example.civic_fix.dto.SystemStatisticsDTO;
import com.example.civic_fix.dto.UpdateRoleRequestDTO;
import com.example.civic_fix.dto.UserResponseDTO;
import com.example.civic_fix.service.AuditService;
import com.example.civic_fix.service.NotificationService;
import com.example.civic_fix.service.UserService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public AdminController(UserService userService, AuditService auditService, NotificationService notificationService) {
        this.userService = userService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/role/{role}")
    public List<UserResponseDTO> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }

    @GetMapping("/users/department/{departmentId}")
    public List<UserResponseDTO> getUsersByDepartment(@PathVariable Long departmentId) {
        return userService.getUsersByDepartment(departmentId);
    }

    @PutMapping("/users/{userId}/role")
    public UserResponseDTO updateUserRole(
            @PathVariable Long userId,
            @RequestBody UpdateRoleRequestDTO request) {
        return userService.updateUserRole(userId, request);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @GetMapping("/statistics")
    public SystemStatisticsDTO getSystemStatistics() {
        return userService.getSystemStatistics();
    }

    @GetMapping("/audit-logs")
    public AuditLogPageDTO getAuditLogs(
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    
        return auditService.getAuditLogs(fromDate, toDate, page, size);
    }

    @PostMapping("/notifications/broadcast")
    public BroadcastResponseDTO broadcastNotification(@RequestBody BroadcastNotificationRequest request) {
        return notificationService.broadcastNotification(request);
    }

    @GetMapping("/notifications")
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}