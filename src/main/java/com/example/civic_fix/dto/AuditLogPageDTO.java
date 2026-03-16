package com.example.civic_fix.dto;

import java.util.List;

import lombok.Data;

@Data
public class AuditLogPageDTO {
    private List<AuditLogDTO> content;
    private PageableDTO pageable;
    private long totalElements;
    private int totalPages;
}

