package com.example.civic_fix.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.civic_fix.dto.AssignIssueRequest;
import com.example.civic_fix.dto.BulkAssignRequest;
import com.example.civic_fix.dto.BulkAssignResult;
import com.example.civic_fix.dto.CreateIssueRequest;
import com.example.civic_fix.dto.IssueResponseDTO;
import com.example.civic_fix.dto.ResolveIssueRequest;
import com.example.civic_fix.service.IssueService;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // CITIZEN creates issue
    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping
    public IssueResponseDTO createIssue(@RequestBody CreateIssueRequest request) {
        return issueService.createIssue(request);
    }

    // ✅ ALL authenticated users can read a single issue (citizens need for IssueDetail)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public IssueResponseDTO getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    // ✅ ALL authenticated users can read all issues (citizens need for Civic Feed)
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<IssueResponseDTO> getAllIssues() {
        return issueService.getAllIssues();
    }

    // ✅ ALL authenticated users can read their own issues
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public List<IssueResponseDTO> getIssuesByUser(@PathVariable Long userId) {
        return issueService.getIssuesByUser(userId);
    }

    // OFFICER and ADMIN can read by department
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    @GetMapping("/department/{deptId}")
    public List<IssueResponseDTO> getIssuesByDepartment(@PathVariable Long deptId) {
        return issueService.getIssuesByDepartment(deptId);
    }

    // ✅ ALL authenticated users can filter by status
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/status/{status}")
    public List<IssueResponseDTO> getIssuesByStatus(@PathVariable String status) {
        return issueService.getIssuesByStatus(status);
    }

    // ADMIN assigns issue
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public IssueResponseDTO assignIssue(@RequestBody AssignIssueRequest request) {
        return issueService.assignIssue(request);
    }

    // ADMIN bulk assigns
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bulk-assign")
    public BulkAssignResult bulkAssignIssues(@RequestBody BulkAssignRequest request) {
        return issueService.bulkAssignIssues(request);
    }

    // ADMIN sees unassigned
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned")
    public List<IssueResponseDTO> getUnassignedIssues() {
        return issueService.getUnassignedIssues();
    }

    // ✅ OFFICER and ADMIN can resolve
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    @PutMapping("/{issueId}/resolve")
    public IssueResponseDTO resolveIssue(
            @PathVariable Long issueId,
            @RequestBody ResolveIssueRequest request) {
        return issueService.resolveIssue(issueId, request);
    }
}