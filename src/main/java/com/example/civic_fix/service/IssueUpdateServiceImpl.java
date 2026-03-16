package com.example.civic_fix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.IssueUpdate;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.Repository.IssueUpdateRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.IssueUpdateRequestDTO;
import com.example.civic_fix.dto.IssueUpdateResponseDTO;
import com.example.civic_fix.mapper.DTOMapper;

@Service
public class IssueUpdateServiceImpl implements IssueUpdateService {

    private final IssueUpdateRepository issueUpdateRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;  // Add this
    private final DTOMapper dtoMapper;

    public IssueUpdateServiceImpl(IssueUpdateRepository issueUpdateRepository,
                                 IssueRepository issueRepository,
                                 UserRepository userRepository,  // Add this
                                 DTOMapper dtoMapper) {
        this.issueUpdateRepository = issueUpdateRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;  // Add this
        this.dtoMapper = dtoMapper;
    }

    @Override
    public IssueUpdateResponseDTO addUpdate(IssueUpdateRequestDTO request) {
        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        // Get current user ID from security context
        String userId = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
        
        User currentUser = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        IssueUpdate issueUpdate = new IssueUpdate();
        issueUpdate.setIssue(issue);
        issueUpdate.setUpdatedBy(currentUser);
        issueUpdate.setMessage(request.getMessage());
        issueUpdate.setStatusAfterUpdate(request.getStatusAfterUpdate());

        if (request.getStatusAfterUpdate() != null) {
            issue.updateStatus(request.getStatusAfterUpdate());
            issueRepository.save(issue);
        }
        
        IssueUpdate savedUpdate = issueUpdateRepository.save(issueUpdate);
        return dtoMapper.toIssueUpdateDTO(savedUpdate);
    }

    @Override
    public List<IssueUpdateResponseDTO> getUpdatesByIssue(Long issueId) {
        List<IssueUpdate> updates = issueUpdateRepository.findByIssue_IssueId(issueId);
        return dtoMapper.toIssueUpdateDTOList(updates);
    }
}