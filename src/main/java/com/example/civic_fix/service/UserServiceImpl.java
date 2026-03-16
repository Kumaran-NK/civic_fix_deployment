package com.example.civic_fix.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.civic_fix.Entity.Department;
import com.example.civic_fix.Entity.IssueStatus;
import com.example.civic_fix.Entity.Role;
import com.example.civic_fix.Entity.User;
import com.example.civic_fix.Repository.DepartmentRepository;
import com.example.civic_fix.Repository.UserRepository;
import com.example.civic_fix.dto.DepartmentIssueCountDTO;
import com.example.civic_fix.dto.LoginRequest;
import com.example.civic_fix.dto.SystemStatisticsDTO;
import com.example.civic_fix.dto.UpdateRoleRequestDTO;
import com.example.civic_fix.dto.UserRequestDTO;
import com.example.civic_fix.dto.UserResponseDTO;
import com.example.civic_fix.mapper.DTOMapper;

import java.time.LocalDateTime;
import java.util.*;

import com.example.civic_fix.Repository.IssueRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DTOMapper dtoMapper;
    private final IssueRepository issueRepository;

    public UserServiceImpl(UserRepository userRepository, 
                          DepartmentRepository departmentRepository,
                          PasswordEncoder passwordEncoder,
                          DTOMapper dtoMapper,
                        IssueRepository issueRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoMapper = dtoMapper;
        this.issueRepository = issueRepository;
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        // Set role
        try {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role specified");
        }

        // Set department if officer/admin
        if (request.getDepartmentId() != null && 
            (user.getRole() == Role.OFFICER || user.getRole() == Role.ADMIN)) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(department);
        }

        user.setZone(request.getZone());
        
        User savedUser = userRepository.save(user);
        return dtoMapper.toUserDTO(savedUser);
    }

    @Override
    public UserResponseDTO login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        return dtoMapper.toUserDTO(user);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return dtoMapper.toUserDTO(user);
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        
        String userId = authentication.getPrincipal().toString();
        return getUserById(Long.parseLong(userId));
    }

    @Override
public List<UserResponseDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return dtoMapper.toUserDTOList(users);
}

@Override
public List<UserResponseDTO> getUsersByRole(String role) {
    try {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        List<User> users = userRepository.findByRole(roleEnum);
        return dtoMapper.toUserDTOList(users);
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid role: " + role);
    }
}

@Override
public List<UserResponseDTO> getUsersByDepartment(Long departmentId) {
    List<User> users = userRepository.findByDepartment_DepartmentId(departmentId);
    return dtoMapper.toUserDTOList(users);
}

@Override
public UserResponseDTO updateUserRole(Long userId, UpdateRoleRequestDTO request) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
    Role newRole;
    try {
        newRole = Role.valueOf(request.getRole().toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid role: " + request.getRole());
    }
    
    user.setRole(newRole);
    
    if (newRole == Role.OFFICER) {
        if (request.getDepartmentId() == null) {
            throw new RuntimeException("Department ID is required for OFFICER role");
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        user.setDepartment(department);
    } else {
        user.setDepartment(null); 
    }
    
    User updatedUser = userRepository.save(user);
    return dtoMapper.toUserDTO(updatedUser);
}

@Override
public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
    // Check if user has any reported issues
    long issuesCount = issueRepository.countByReportedBy_UserId(userId);
    if (issuesCount > 0) {
        throw new RuntimeException("Cannot delete user with " + issuesCount + 
                                  " reported issues. Archive or reassign issues first.");
    }
    
    // Check if user is assigned as officer to any issues
    if (user.getRole() == Role.OFFICER) {
        long assignedIssuesCount = issueRepository.countByAssignedOfficer_UserId(userId);
        if (assignedIssuesCount > 0) {
            throw new RuntimeException("Cannot delete officer with " + assignedIssuesCount + 
                                      " assigned issues. Reassign issues first.");
        }
    }
    
    userRepository.delete(user);
}

    @Override
    public SystemStatisticsDTO getSystemStatistics() {
        SystemStatisticsDTO stats = new SystemStatisticsDTO();
        
        // User statistics
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCitizens(userRepository.countByRole(Role.CITIZEN));
        stats.setTotalOfficers(userRepository.countByRole(Role.OFFICER));
        stats.setTotalAdmins(userRepository.countByRole(Role.ADMIN));
        
        // Issue statistics
        stats.setTotalIssues(issueRepository.count());
        
        // Issues by status
        Map<String, Long> issuesByStatus = new HashMap<>();
        for (IssueStatus status : IssueStatus.values()) {
            long count = issueRepository.countByStatus(status);
            issuesByStatus.put(status.name(), count);
        }
        stats.setIssuesByStatus(issuesByStatus);
        
        // Issues by department
        List<DepartmentIssueCountDTO> deptStats = new ArrayList<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department dept : departments) {
            long count = issueRepository.countByAssignedDepartment_DepartmentId(dept.getDepartmentId());
            if (count > 0) {
                DepartmentIssueCountDTO dto = new DepartmentIssueCountDTO();
                dto.setDepartmentId(dept.getDepartmentId());
                dto.setDepartmentName(dept.getDepartmentName());
                dto.setIssueCount(count);
                deptStats.add(dto);
            }
        }
        stats.setIssuesByDepartment(deptStats);
        
        // Average resolution time (in days)
        Double avgResolutionTime = issueRepository.getAverageResolutionTime();
        stats.setAverageResolutionTime(avgResolutionTime != null ? 
                                    String.format("%.1f days", avgResolutionTime) : "N/A");
        
        // Today's statistics
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        stats.setIssuesReportedToday(issueRepository.countByCreatedAtBetween(startOfDay, endOfDay));
        stats.setIssuesResolvedToday(issueRepository.countByStatusAndUpdatedAtBetween(
            IssueStatus.RESOLVED, startOfDay, endOfDay));
        
        return stats;
    }
}