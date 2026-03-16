package com.example.civic_fix.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.civic_fix.dto.IssueUpdateRequestDTO;
import com.example.civic_fix.dto.IssueUpdateResponseDTO;
import com.example.civic_fix.service.IssueUpdateService;

@RestController
@RequestMapping("/api/issue-updates")
public class IssueUpdateController {

    private final IssueUpdateService issueUpdateService;

    public IssueUpdateController(IssueUpdateService issueUpdateService) {
        this.issueUpdateService = issueUpdateService;
    }

    // ✅ Allow OFFICER and ADMIN to post updates
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    @PostMapping
    public IssueUpdateResponseDTO addUpdate(@RequestBody IssueUpdateRequestDTO request) {
        return issueUpdateService.addUpdate(request);
    }

    // ✅ Allow all authenticated users to read updates (citizens need this for IssueDetail)
    @GetMapping("/issue/{issueId}")
    public List<IssueUpdateResponseDTO> getUpdatesByIssue(@PathVariable Long issueId) {
        return issueUpdateService.getUpdatesByIssue(issueId);
    }
}