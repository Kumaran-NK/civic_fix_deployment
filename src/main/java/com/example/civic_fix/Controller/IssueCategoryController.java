package com.example.civic_fix.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.civic_fix.dto.IssueCategoryRequestDTO;
import com.example.civic_fix.dto.IssueCategoryResponseDTO;
import com.example.civic_fix.service.IssueCategoryService;

@RestController
@RequestMapping("/api/issue-categories")
public class IssueCategoryController {
    private final IssueCategoryService issueCategoryService;

    public IssueCategoryController(IssueCategoryService issueCategoryService) {
        this.issueCategoryService = issueCategoryService;
    }

    @PostMapping
    public IssueCategoryResponseDTO createCategory(@RequestBody IssueCategoryRequestDTO request) {
        return issueCategoryService.createIssueCategory(request);
    }

    @GetMapping
    public List<IssueCategoryResponseDTO> getAllCategories() {
        return issueCategoryService.getAllCategories();
    }
    
    @GetMapping("/{categoryId}")
    public IssueCategoryResponseDTO getCategoryById(@PathVariable Long categoryId) {
        return issueCategoryService.getCategoryById(categoryId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public IssueCategoryResponseDTO updateCategory(
            @PathVariable Long categoryId,
            @RequestBody IssueCategoryRequestDTO request) {
        return issueCategoryService.updateIssueCategory(categoryId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        issueCategoryService.deleteIssueCategory(categoryId);
        return ResponseEntity.ok(Map.of("message", "Issue category deleted successfully"));
    }
}