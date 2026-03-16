package com.example.civic_fix.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.civic_fix.Entity.Attachment;
import com.example.civic_fix.Entity.Department;
import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.IssueCategory;
import com.example.civic_fix.Entity.IssueUpdate;
import com.example.civic_fix.Entity.Notification;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.dto.AttachmentResponseDTO;
import com.example.civic_fix.dto.DepartmentResponseDTO;
import com.example.civic_fix.dto.IssueCategoryResponseDTO;
import com.example.civic_fix.dto.IssueResponseDTO;
import com.example.civic_fix.dto.IssueUpdateResponseDTO;
import com.example.civic_fix.dto.NotificationResponseDTO;
import com.example.civic_fix.dto.UserResponseDTO;

@Component
public class DTOMapper {
    
    public UserResponseDTO toUserDTO(User user) {
        if (user == null) return null;
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().name());
        dto.setZone(user.getZone());
        dto.setCreatedAt(user.getCreatedAt());
        
        if (user.getDepartment() != null) {
            dto.setDepartmentId(user.getDepartment().getDepartmentId());
            dto.setDepartmentName(user.getDepartment().getDepartmentName());
        }
        
        return dto;
    }
 
    public DepartmentResponseDTO toDepartmentDTO(Department department) {
        if (department == null) return null;
        
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setDescription(department.getDescription());
        return dto;
    }
    
    public IssueCategoryResponseDTO toIssueCategoryDTO(IssueCategory category) {
        if (category == null) return null;
        
        IssueCategoryResponseDTO dto = new IssueCategoryResponseDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setBaseScore(category.getBaseScore());
        dto.setDepartment(toDepartmentDTO(category.getDepartment()));
        return dto;
    }
    
    public IssueResponseDTO toIssueDTO(Issue issue) {
        if (issue == null) return null;
        
        IssueResponseDTO dto = new IssueResponseDTO();
        dto.setIssueId(issue.getIssueId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        
        // Handle status - now using enum
        if (issue.getStatus() != null) {
            dto.setStatus(issue.getStatus());
            dto.setStatusDisplayName(issue.getStatus().getDisplayName());
        }
        
        dto.setLatitude(issue.getLatitude());
        dto.setLongitude(issue.getLongitude());
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        dto.setAssignedAt(issue.getAssignedAt());
        
        // Reported By information
        if (issue.getReportedBy() != null) {
            dto.setReportedById(issue.getReportedBy().getUserId());
            dto.setReportedByName(issue.getReportedBy().getName());
            dto.setReportedByEmail(issue.getReportedBy().getEmail());
        }
        
        // Category information
        if (issue.getCategory() != null) {
            dto.setCategoryId(issue.getCategory().getCategoryId());
            dto.setCategoryName(issue.getCategory().getName());
        }
        
        // Assigned Department information
        if (issue.getAssignedDepartment() != null) {
            dto.setAssignedDepartmentId(issue.getAssignedDepartment().getDepartmentId());
            dto.setAssignedDepartmentName(issue.getAssignedDepartment().getDepartmentName());
        }
        
        // Assigned Officer information
        if (issue.getAssignedOfficer() != null) {
            dto.setAssignedOfficerId(issue.getAssignedOfficer().getUserId());
            dto.setAssignedOfficerName(issue.getAssignedOfficer().getName());
        }
        
        return dto;
    }
    
    public IssueUpdateResponseDTO toIssueUpdateDTO(IssueUpdate update) {
        if (update == null) return null;
        
        IssueUpdateResponseDTO dto = new IssueUpdateResponseDTO();
        dto.setUpdateId(update.getUpdateId());
        
        if (update.getIssue() != null) {
            dto.setIssueId(update.getIssue().getIssueId());
            dto.setIssueTitle(update.getIssue().getTitle());
        }
        
        if (update.getUpdatedBy() != null) {
            dto.setUpdatedById(update.getUpdatedBy().getUserId());
            dto.setUpdatedByName(update.getUpdatedBy().getName());
        }
        
        dto.setMessage(update.getMessage());
        
        // Handle status after update - now using enum
        if (update.getStatusAfterUpdate() != null) {
            dto.setStatusAfterUpdate(update.getStatusAfterUpdate());
            dto.setStatusDisplayName(update.getStatusAfterUpdate().getDisplayName());
        }
        
        dto.setCreatedAt(update.getCreatedAt());
        
        return dto;
    }
    
    public AttachmentResponseDTO toAttachmentDTO(Attachment attachment) {
        if (attachment == null) return null;
        
        AttachmentResponseDTO dto = new AttachmentResponseDTO();
        dto.setAttachmentId(attachment.getAttachmentId());
        
        if (attachment.getIssue() != null) {
            dto.setIssueId(attachment.getIssue().getIssueId());
        }
        
        if (attachment.getUploadedBy() != null) {
            dto.setUploadedById(attachment.getUploadedBy().getUserId());
            dto.setUploadedByName(attachment.getUploadedBy().getName());
        }
        
        dto.setFileUrl(attachment.getFileUrl());
        dto.setFileType(attachment.getFileType());
        dto.setUploadedAt(attachment.getUploadedAt());
        
        return dto;
    }
    
    public NotificationResponseDTO toNotificationDTO(Notification notification) {
        if (notification == null) return null;
        
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setNotificationId(notification.getNotificationId());
        
        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getUserId());
            dto.setUserName(notification.getUser().getName());
        }
        
        if (notification.getIssue() != null) {
            dto.setIssueId(notification.getIssue().getIssueId());
            dto.setIssueTitle(notification.getIssue().getTitle());
        }
        
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        
        return dto;
    }
    
    // List Mappings
    public List<UserResponseDTO> toUserDTOList(List<User> users) {
        if (users == null) return null;
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }
    
    public List<DepartmentResponseDTO> toDepartmentDTOList(List<Department> departments) {
        if (departments == null) return null;
        return departments.stream()
                .map(this::toDepartmentDTO)
                .collect(Collectors.toList());
    }
    
    public List<IssueCategoryResponseDTO> toIssueCategoryDTOList(List<IssueCategory> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(this::toIssueCategoryDTO)
                .collect(Collectors.toList());
    }
    
    public List<IssueResponseDTO> toIssueDTOList(List<Issue> issues) {
        if (issues == null) return null;
        return issues.stream()
                .map(this::toIssueDTO)
                .collect(Collectors.toList());
    }
    
    public List<IssueUpdateResponseDTO> toIssueUpdateDTOList(List<IssueUpdate> updates) {
        if (updates == null) return null;
        return updates.stream()
                .map(this::toIssueUpdateDTO)
                .collect(Collectors.toList());
    }
    
    public List<AttachmentResponseDTO> toAttachmentDTOList(List<Attachment> attachments) {
        if (attachments == null) return null;
        return attachments.stream()
                .map(this::toAttachmentDTO)
                .collect(Collectors.toList());
    }
    
    public List<NotificationResponseDTO> toNotificationDTOList(List<Notification> notifications) {
        if (notifications == null) return null;
        return notifications.stream()
                .map(this::toNotificationDTO)
                .collect(Collectors.toList());
    }
}