package com.example.civic_fix.service;

import java.util.List;

import com.example.civic_fix.dto.IssueUpdateRequestDTO;
import com.example.civic_fix.dto.IssueUpdateResponseDTO;

public interface IssueUpdateService {
    IssueUpdateResponseDTO addUpdate(IssueUpdateRequestDTO request);
    
    List<IssueUpdateResponseDTO> getUpdatesByIssue(Long issueId);
}