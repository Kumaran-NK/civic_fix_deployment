package com.example.civic_fix.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.civic_fix.Entity.Role;
import com.example.civic_fix.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByDepartment_DepartmentId(Long departmentId);
    Optional<User> findById(Long userId);
    List<User> findByRole(Role role);
    long countByRole(Role role);
    long countByDepartment_DepartmentId(Long departmentId);
}