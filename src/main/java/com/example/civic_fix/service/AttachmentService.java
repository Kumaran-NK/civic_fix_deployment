package com.example.civic_fix.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.civic_fix.dto.AttachmentResponseDTO;

public interface AttachmentService {
    AttachmentResponseDTO uploadAttachment(Long issueId, MultipartFile file);
    List<AttachmentResponseDTO> getAttachmentsByIssue(Long issueId);
    AttachmentResponseDTO getAttachmentById(Long attachmentId);
}