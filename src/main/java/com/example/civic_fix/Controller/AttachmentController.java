package com.example.civic_fix.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.civic_fix.dto.AttachmentResponseDTO;
import com.example.civic_fix.service.AttachmentService;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    // ✅ CITIZEN, OFFICER, and ADMIN can upload attachments
    // Officers need this to attach resolution proof photos
    @PreAuthorize("hasAnyRole('CITIZEN', 'OFFICER', 'ADMIN')")
    @PostMapping
    public AttachmentResponseDTO uploadAttachment(
            @RequestParam Long issueId,
            @RequestParam MultipartFile file) {
        return attachmentService.uploadAttachment(issueId, file);
    }

    // ✅ All authenticated users can view attachments
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/issue/{issueId}")
    public List<AttachmentResponseDTO> getAttachmentsByIssue(@PathVariable Long issueId) {
        return attachmentService.getAttachmentsByIssue(issueId);
    }

    // ✅ All authenticated users can download
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        AttachmentResponseDTO attachment = attachmentService.getAttachmentById(attachmentId);

        Path filePath = Paths.get(attachment.getFileUrl());

        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + attachment.getFileUrl());
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        attachment.getFileType() != null
                                ? attachment.getFileType()
                                : "application/octet-stream"
                ))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + filePath.getFileName() + "\"")
                .body(resource);
    }
}