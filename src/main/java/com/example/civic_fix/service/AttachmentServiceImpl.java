package com.example.civic_fix.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.civic_fix.Entity.Attachment;
import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.Repository.AttachmentRepository;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.AttachmentResponseDTO;
import com.example.civic_fix.mapper.DTOMapper;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    private final AttachmentRepository attachmentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final DTOMapper dtoMapper;

    public AttachmentServiceImpl(
            AttachmentRepository attachmentRepository,
            IssueRepository issueRepository,
            UserRepository userRepository,
            DTOMapper dtoMapper) {
        this.attachmentRepository = attachmentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public AttachmentResponseDTO uploadAttachment(Long issueId, MultipartFile file) {
        // Get current user from JWT
        Long userId = Long.parseLong(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
        );

        Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new RuntimeException("Issue not found"));

        User uploader = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename to avoid collisions
            String originalName = file.getOriginalFilename();
            String extension    = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
            String uniqueName   = UUID.randomUUID().toString() + extension;

            // Save file to disk
            Path filePath = uploadPath.resolve(uniqueName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // ✅ Store RELATIVE path (e.g. "uploads/abc123.jpg")
            // This is used by frontend as: http://localhost:8080/uploads/abc123.jpg
            String relativeUrl = uploadDir + uniqueName;

            Attachment attachment = new Attachment();
            attachment.setIssue(issue);
            attachment.setUploadedBy(uploader);
            attachment.setFileUrl(relativeUrl);               // relative path
            attachment.setFileType(file.getContentType());    // e.g. "image/jpeg", "video/mp4"

            Attachment saved = attachmentRepository.save(attachment);
            return dtoMapper.toAttachmentDTO(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public List<AttachmentResponseDTO> getAttachmentsByIssue(Long issueId) {
        List<Attachment> attachments = attachmentRepository.findByIssue_IssueId(issueId);
        return dtoMapper.toAttachmentDTOList(attachments);
    }

    @Override
    public AttachmentResponseDTO getAttachmentById(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found"));
        return dtoMapper.toAttachmentDTO(attachment);
    }
}