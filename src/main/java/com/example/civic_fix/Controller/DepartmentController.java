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

import com.example.civic_fix.dto.DepartmentRequestDTO;
import com.example.civic_fix.dto.DepartmentResponseDTO;
import com.example.civic_fix.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController { 
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public DepartmentResponseDTO createDepartment(@RequestBody DepartmentRequestDTO department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{departmentId}")
    public DepartmentResponseDTO getDepartmentById(@PathVariable Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{departmentId}")
    public DepartmentResponseDTO updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody DepartmentRequestDTO request) {
        return departmentService.updateDepartment(departmentId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }
}