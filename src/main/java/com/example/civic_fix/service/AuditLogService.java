package com.example.civic_fix.service;

import com.example.civic_fix.Entity.AuditLog;
import com.example.civic_fix.Repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public void log(String action, String entityType, Long entityId, 
                   Long userId, String username, String details, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setUserId(userId);
        log.setUsername(username);
        log.setDetails(details);
        log.setIpAddress(getClientIp(request));
        
        auditLogRepository.save(log);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}