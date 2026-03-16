package com.example.civic_fix.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AttachmentResponseDTO {
    private Long attachmentId;
    private Long issueId;
    private Long uploadedById;
    private String uploadedByName;
    private String fileUrl;
    private String fileType;
    private LocalDateTime uploadedAt;
}