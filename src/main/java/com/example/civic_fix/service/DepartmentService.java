package com.example.civic_fix.service;

import java.util.List;

import com.example.civic_fix.dto.DepartmentRequestDTO;
import com.example.civic_fix.dto.DepartmentResponseDTO;

public interface DepartmentService {
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO getDepartmentById(Long departmentId);

    DepartmentResponseDTO updateDepartment(Long departmentId, DepartmentRequestDTO request);

    void deleteDepartment(Long departmentId);

    


}