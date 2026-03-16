package com.example.civic_fix.service;

import java.util.List;

import com.example.civic_fix.dto.AssignIssueRequest;
import com.example.civic_fix.dto.BulkAssignRequest;
import com.example.civic_fix.dto.BulkAssignResult;
import com.example.civic_fix.dto.CreateIssueRequest;
import com.example.civic_fix.dto.IssueResponseDTO;
import com.example.civic_fix.dto.ReassignIssueRequest;
import com.example.civic_fix.dto.ResolveIssueRequest;

public interface IssueService {
    IssueResponseDTO createIssue(CreateIssueRequest request);

    IssueResponseDTO getIssueById(Long issueId);

    List<IssueResponseDTO> getAllIssues();

    List<IssueResponseDTO> getIssuesByUser(Long userId);

    List<IssueResponseDTO> getIssuesByDepartment(Long departmentId);

    List<IssueResponseDTO> getIssuesByStatus(String status);
    
    IssueResponseDTO assignIssue(AssignIssueRequest request);

    IssueResponseDTO reassignIssue(Long issueId, ReassignIssueRequest request);

    BulkAssignResult bulkAssignIssues(BulkAssignRequest request);

    List<IssueResponseDTO> getUnassignedIssues();

    IssueResponseDTO resolveIssue(Long issueId, ResolveIssueRequest request);
}