package com.example.civic_fix.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Department;
import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.IssueCategory;
import com.example.civic_fix.Entity.IssueStatus;
import com.example.civic_fix.Entity.IssueUpdate;
import com.example.civic_fix.Entity.Notification;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.Repository.DepartmentRepository;
import com.example.civic_fix.Repository.IssueCategoryRepository;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.Repository.IssueUpdateRepository;
import com.example.civic_fix.Repository.NotificationRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.AssignIssueRequest;
import com.example.civic_fix.dto.BulkAssignItemResult;
import com.example.civic_fix.dto.BulkAssignRequest;
import com.example.civic_fix.dto.BulkAssignResult;
import com.example.civic_fix.dto.CreateIssueRequest;
import com.example.civic_fix.dto.IssueResponseDTO;
import com.example.civic_fix.dto.ReassignIssueRequest;
import com.example.civic_fix.dto.ResolveIssueRequest;
import com.example.civic_fix.mapper.DTOMapper;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final IssueCategoryRepository issueCategoryRepository;
    private final DepartmentRepository departmentRepository;
    private final DTOMapper dtoMapper;
    private final IssueUpdateRepository issueUpdateRepository;
    private final NotificationRepository notificationRepository;
    
    public IssueServiceImpl(
            IssueRepository issueRepository,
            UserRepository userRepository,
            IssueCategoryRepository issueCategoryRepository,
            DepartmentRepository departmentRepository,
            DTOMapper dtoMapper,
            IssueUpdateRepository issueUpdateRepository,
            NotificationRepository notificationRepository) {

        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.issueCategoryRepository = issueCategoryRepository;
        this.departmentRepository = departmentRepository;
        this.dtoMapper = dtoMapper;
        this.issueUpdateRepository = issueUpdateRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public IssueResponseDTO createIssue(CreateIssueRequest request) {
        Long userId = Long.parseLong(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );

        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IssueCategory category = issueCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Issue issue = new Issue();
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setReportedBy(reporter);
        issue.setCategory(category);
        issue.setLatitude(request.getLatitude());
        issue.setLongitude(request.getLongitude());
        
        // FIX: Use enum instead of String
        issue.setStatus(IssueStatus.REPORTED);  // Changed from "REPORTED"
        
        issue.setAssignedDepartment(null);
        issue.setAssignedOfficer(null);

        Issue savedIssue = issueRepository.save(issue);
        return dtoMapper.toIssueDTO(savedIssue);
    }

    @Override
    public IssueResponseDTO getIssueById(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        return dtoMapper.toIssueDTO(issue);
    }

    @Override
    public List<IssueResponseDTO> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        return dtoMapper.toIssueDTOList(issues);
    }

    @Override
    public List<IssueResponseDTO> getIssuesByUser(Long userId) {
        List<Issue> issues = issueRepository.findByReportedBy_UserId(userId);
        return dtoMapper.toIssueDTOList(issues);
    }

    @Override
    public List<IssueResponseDTO> getIssuesByDepartment(Long departmentId) {
        List<Issue> issues = issueRepository.findByAssignedDepartment_DepartmentId(departmentId);
        return dtoMapper.toIssueDTOList(issues);
    }

    @Override
    public List<IssueResponseDTO> getIssuesByStatus(String status) {
        // FIX: Convert String to enum for repository query
        // Option 1: If repository still uses String
        // List<Issue> issues = issueRepository.findByStatus(status);
        
        // Option 2: If repository is updated to use enum (recommended)
        IssueStatus statusEnum = IssueStatus.valueOf(status.toUpperCase());
        List<Issue> issues = issueRepository.findByStatus(statusEnum);
        
        return dtoMapper.toIssueDTOList(issues);
    }

    @Override
    public IssueResponseDTO assignIssue(AssignIssueRequest request) {
        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        if (!officer.getRole().name().equals("OFFICER")) {
            throw new RuntimeException("User is not an officer");
        }

        issue.setAssignedDepartment(department);
        issue.setAssignedOfficer(officer);
        issue.setAssignedAt(LocalDateTime.now());
        
       
        issue.setStatus(IssueStatus.ASSIGNED); 

        Issue updatedIssue = issueRepository.save(issue);
        return dtoMapper.toIssueDTO(updatedIssue);
    }


    @Override
public IssueResponseDTO reassignIssue(Long issueId, ReassignIssueRequest request) {
    Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new RuntimeException("Issue not found"));
    
    Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Department not found"));
    
    User officer = userRepository.findById(request.getOfficerId())
            .orElseThrow(() -> new RuntimeException("Officer not found"));
    
    if (!officer.getRole().name().equals("OFFICER")) {
        throw new RuntimeException("User is not an officer");
    }
    
    // Store old assignment for audit
    Long oldDeptId = issue.getAssignedDepartment() != null ? 
                     issue.getAssignedDepartment().getDepartmentId() : null;
    Long oldOfficerId = issue.getAssignedOfficer() != null ? 
                        issue.getAssignedOfficer().getUserId() : null;
    
    issue.setAssignedDepartment(department);
    issue.setAssignedOfficer(officer);
    issue.setAssignedAt(LocalDateTime.now());
    issue.setStatus(IssueStatus.ASSIGNED);
    
    // Create an update with reassignment reason
    if (request.getReason() != null && !request.getReason().isEmpty()) {
        // Get current user
        String userId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
        
        User currentUser = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        IssueUpdate update = new IssueUpdate();
        update.setIssue(issue);
        update.setUpdatedBy(currentUser);
        update.setMessage("Reassigned: " + request.getReason() + 
                         " (From Dept: " + (oldDeptId != null ? oldDeptId : "None") + 
                         ", Officer: " + (oldOfficerId != null ? oldOfficerId : "None") + ")");
        update.setStatusAfterUpdate(IssueStatus.ASSIGNED);
        issueUpdateRepository.save(update);
    }
    
    Issue updatedIssue = issueRepository.save(issue);
    return dtoMapper.toIssueDTO(updatedIssue);
}
    @Override
    public BulkAssignResult bulkAssignIssues(BulkAssignRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        
        if (!officer.getRole().name().equals("OFFICER")) {
            throw new RuntimeException("User is not an officer");
        }
        
        List<BulkAssignItemResult> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;
        
        for (Long issueId : request.getIssueId()) {
            BulkAssignItemResult itemResult = new BulkAssignItemResult();
            itemResult.setIssueId(issueId);
            
            try {
                Issue issue = issueRepository.findById(issueId)
                        .orElseThrow(() -> new RuntimeException("Issue not found"));
                
                issue.setAssignedDepartment(department);
                issue.setAssignedOfficer(officer);
                issue.setAssignedAt(LocalDateTime.now());
                issue.setStatus(IssueStatus.ASSIGNED);
                
                issueRepository.save(issue);
                
                itemResult.setStatus("ASSIGNED");
                itemResult.setMessage("Successfully assigned");
                successCount++;
            } catch (Exception e) {
                itemResult.setStatus("FAILED");
                itemResult.setMessage(e.getMessage());
                failedCount++;
            }
            
            results.add(itemResult);
        }
        
        BulkAssignResult result = new BulkAssignResult();
        result.setSuccessCount(successCount);
        result.setFailedCount(failedCount);
        result.setResults(results);
        
        return result;
    }

    @Override
    public List<IssueResponseDTO> getUnassignedIssues() {
    // Find issues with status REPORTED and no assigned department/officer
    List<Issue> unassignedIssues = issueRepository.findByStatusAndAssignedDepartmentIsNull(IssueStatus.REPORTED);
    return dtoMapper.toIssueDTOList(unassignedIssues);
}


    @Override
public IssueResponseDTO resolveIssue(Long issueId, ResolveIssueRequest request) {
    Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new RuntimeException("Issue not found"));
    
    // Check if issue can be resolved
    if (issue.getStatus() != IssueStatus.IN_PROGRESS && 
        issue.getStatus() != IssueStatus.ASSIGNED) {
        throw new RuntimeException("Issue must be IN_PROGRESS or ASSIGNED to be resolved");
    }
    
    // Get current user
    String userId = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();
    
    User currentUser = userRepository.findById(Long.parseLong(userId))
            .orElseThrow(() -> new RuntimeException("User not found"));
    
    // Update issue status
    issue.setStatus(IssueStatus.RESOLVED);
    Issue updatedIssue = issueRepository.save(issue);
    
    // Create update with resolution notes
    IssueUpdate update = new IssueUpdate();
    update.setIssue(issue);
    update.setUpdatedBy(currentUser);
    
    String resolutionMessage = "Issue resolved";
    if (request.getResolutionNotes() != null && !request.getResolutionNotes().isEmpty()) {
        resolutionMessage += ": " + request.getResolutionNotes();
    }
    
    if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
        resolutionMessage += " (Attachments: " + String.join(", ", request.getAttachments()) + ")";
    }
    
    update.setMessage(resolutionMessage);
    update.setStatusAfterUpdate(IssueStatus.RESOLVED);
    issueUpdateRepository.save(update);
    
    // Create notification for the citizen who reported the issue
    Notification notification = new Notification();
    notification.setUser(issue.getReportedBy());
    notification.setIssue(issue);
    notification.setType("ISSUE_RESOLVED");
    notification.setMessage("Your issue '" + issue.getTitle() + "' has been resolved");
    notification.setIsRead(false);
    notificationRepository.save(notification);
    
    return dtoMapper.toIssueDTO(updatedIssue);
}
}