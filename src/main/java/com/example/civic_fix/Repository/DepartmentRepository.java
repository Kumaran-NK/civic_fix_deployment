package com.example.civic_fix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.civic_fix.Entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
    
    boolean existsByDepartmentName(String departmentName);

}
