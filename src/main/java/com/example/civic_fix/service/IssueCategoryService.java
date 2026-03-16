package com.example.civic_fix.service;

import java.util.List;

import com.example.civic_fix.dto.IssueCategoryRequestDTO;
import com.example.civic_fix.dto.IssueCategoryResponseDTO;

public interface IssueCategoryService {
    IssueCategoryResponseDTO createIssueCategory(IssueCategoryRequestDTO request);
    List<IssueCategoryResponseDTO> getAllCategories();
    IssueCategoryResponseDTO getCategoryById(Long categoryId);
    IssueCategoryResponseDTO updateIssueCategory(Long categoryId, IssueCategoryRequestDTO request);
    
    void deleteIssueCategory(Long categoryId);
}