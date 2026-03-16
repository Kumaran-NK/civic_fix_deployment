package com.example.civic_fix.service;

import com.example.civic_fix.dto.LoginRequest;
import com.example.civic_fix.dto.SystemStatisticsDTO;
import com.example.civic_fix.dto.UpdateRoleRequestDTO;
import com.example.civic_fix.dto.UserRequestDTO;
import com.example.civic_fix.dto.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO request);
    UserResponseDTO login(LoginRequest request);
    UserResponseDTO getUserById(Long userId);
    UserResponseDTO getCurrentUser();


    List<UserResponseDTO> getAllUsers();
    List<UserResponseDTO> getUsersByRole(String role);
    List<UserResponseDTO> getUsersByDepartment(Long departmentId);
    UserResponseDTO updateUserRole(Long userId, UpdateRoleRequestDTO request);
    void deleteUser(Long userId);

    SystemStatisticsDTO getSystemStatistics();
}