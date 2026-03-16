package com.example.civic_fix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Department;
import com.example.civic_fix.Repository.DepartmentRepository;
import com.example.civic_fix.Repository.IssueRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.DepartmentRequestDTO;
import com.example.civic_fix.dto.DepartmentResponseDTO;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new RuntimeException("Department already exists");
        }
        
        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department Not found"));
        return convertToDTO(department);
    }
    
    private DepartmentResponseDTO convertToDTO(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setDescription(department.getDescription());
        return dto;
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long departmentId, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        
        if (!department.getDepartmentName().equals(request.getDepartmentName()) && 
            departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new RuntimeException("Department with name '" + request.getDepartmentName() + "' already exists");
        }
        
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }

    @Override
public void deleteDepartment(Long departmentId) {
    Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
    
    
    long assignedIssuesCount = issueRepository.countByAssignedDepartment_DepartmentId(departmentId);
    if (assignedIssuesCount > 0) {
        throw new RuntimeException("Cannot delete department with " + assignedIssuesCount + 
                                  " assigned issues. Reassign issues first.");
    }
    
    // Check if department has any officers
    long officersCount = userRepository.countByDepartment_DepartmentId(departmentId);
    if (officersCount > 0) {
        throw new RuntimeException("Cannot delete department with " + officersCount + 
                                  " officers. Reassign officers first.");
    }
    
    departmentRepository.delete(department);
}
}