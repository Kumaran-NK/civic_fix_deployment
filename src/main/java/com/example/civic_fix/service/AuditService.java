package com.example.civic_fix.service;

import com.example.civic_fix.Entity.AuditLog;
import com.example.civic_fix.Repository.AuditLogRepository;
import com.example.civic_fix.dto.AuditLogDTO;
import com.example.civic_fix.dto.AuditLogPageDTO;
import com.example.civic_fix.dto.PageableDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class AuditService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public AuditLogPageDTO getAuditLogs(String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        
        Page<AuditLog> logPage;
        
        if (fromDate != null && toDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDateTime from = LocalDate.parse(fromDate, formatter).atStartOfDay();
            LocalDateTime to = LocalDate.parse(toDate, formatter).atTime(LocalTime.MAX);
            logPage = auditLogRepository.findByTimestampBetween(from, to, pageable);
        } else {
            logPage = auditLogRepository.findAll(pageable);
        }
        
        return convertToPageDTO(logPage);
    }
    
    private AuditLogPageDTO convertToPageDTO(Page<AuditLog> page) {
        AuditLogPageDTO dto = new AuditLogPageDTO();
        
        dto.setContent(page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        
        PageableDTO pageableDTO = new PageableDTO();
        pageableDTO.setPageNumber(page.getNumber());
        pageableDTO.setPageSize(page.getSize());
        dto.setPageable(pageableDTO);
        
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        
        return dto;
    }
    
    private AuditLogDTO convertToDTO(AuditLog log) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(log.getId());
        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());
        dto.setUserId(log.getUserId());
        dto.setUsername(log.getUsername());
        dto.setDetails(log.getDetails());
        dto.setTimestamp(log.getTimestamp());
        dto.setIpAddress(log.getIpAddress());
        return dto;
    }
}