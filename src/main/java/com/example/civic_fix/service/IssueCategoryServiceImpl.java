package com.example.civic_fix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Department;
import com.example.civic_fix.Entity.IssueCategory;
import com.example.civic_fix.Repository.DepartmentRepository;
import com.example.civic_fix.Repository.IssueCategoryRepository;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.dto.IssueCategoryRequestDTO;
import com.example.civic_fix.dto.IssueCategoryResponseDTO;
import com.example.civic_fix.mapper.DTOMapper;

@Service
public class IssueCategoryServiceImpl implements IssueCategoryService {
    private final IssueCategoryRepository issueCategoryRepository;
    private final DepartmentRepository departmentRepository;
    private final DTOMapper dtoMapper;
    private final IssueRepository issueRepository;

    public IssueCategoryServiceImpl(IssueCategoryRepository issueCategoryRepository,
                                   DepartmentRepository departmentRepository,
                                   DTOMapper dtoMapper,
                                IssueRepository issueRepository) {
        this.issueCategoryRepository = issueCategoryRepository;
        this.departmentRepository = departmentRepository;
        this.dtoMapper = dtoMapper;
        this.issueRepository = issueRepository;
    }

    @Override
    public IssueCategoryResponseDTO createIssueCategory(IssueCategoryRequestDTO request) {
        if (issueCategoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Issue category '" + request.getName() + "' already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        IssueCategory issueCategory = new IssueCategory();
        issueCategory.setName(request.getName());
        issueCategory.setBaseScore(request.getBaseScore());
        issueCategory.setDepartment(department);

        IssueCategory savedCategory = issueCategoryRepository.save(issueCategory);
        return dtoMapper.toIssueCategoryDTO(savedCategory);
    }

    @Override
    public List<IssueCategoryResponseDTO> getAllCategories() {
        List<IssueCategory> categories = issueCategoryRepository.findAll();
        return dtoMapper.toIssueCategoryDTOList(categories);
    }

    @Override
    public IssueCategoryResponseDTO getCategoryById(Long categoryId) {
        IssueCategory category = issueCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category with ID " + categoryId + " does not exist"));
        return dtoMapper.toIssueCategoryDTO(category);
    }


    @Override
    public IssueCategoryResponseDTO updateIssueCategory(Long categoryId, IssueCategoryRequestDTO request) {
    IssueCategory category = issueCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
    
   
    if (!category.getName().equals(request.getName()) && 
        issueCategoryRepository.existsByName(request.getName())) {
        throw new RuntimeException("Issue category '" + request.getName() + "' already exists");
    }
    
    Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Department not found"));
    
    category.setName(request.getName());
    category.setBaseScore(request.getBaseScore());
    category.setDepartment(department);
    
    IssueCategory updatedCategory = issueCategoryRepository.save(category);
    return dtoMapper.toIssueCategoryDTO(updatedCategory);
    }

    @Override
    public void deleteIssueCategory(Long categoryId) {
        IssueCategory category = issueCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        
        long issuesCount = issueRepository.countByCategory_CategoryId(categoryId);
        if (issuesCount > 0) {
            throw new RuntimeException("Cannot delete category with " + issuesCount + 
                                    " issues. Reassign issues to another category first.");
        }
        
        issueCategoryRepository.delete(category);

    }
}