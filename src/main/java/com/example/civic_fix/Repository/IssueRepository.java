package com.example.civic_fix.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.civic_fix.Entity.Issue;
import com.example.civic_fix.Entity.IssueStatus;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByReportedBy_UserId(Long userId);
    
    List<Issue> findByAssignedDepartment_DepartmentId(Long departmentId);
    
    List<Issue> findByStatus(IssueStatus status);
    
    long countByAssignedDepartment_DepartmentId(Long departmentId);
    
    long countByCategory_CategoryId(Long categoryId);
    
    long countByReportedBy_UserId(Long userId);
    
    long countByAssignedOfficer_UserId(Long userId);
    
    long countByStatus(IssueStatus status);
    
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    long countByStatusAndUpdatedAtBetween(IssueStatus status, LocalDateTime start, LocalDateTime end);
    
    List<Issue> findByStatusAndAssignedDepartmentIsNull(IssueStatus status);
    
    // Fixed query for average resolution time - MySQL compatible
    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, i.createdAt, i.updatedAt)) FROM Issue i WHERE i.status = :status AND i.updatedAt IS NOT NULL")
    Double getAverageResolutionTimeForStatus(@Param("status") IssueStatus status);
    
    // Convenience method for RESOLVED status
    default Double getAverageResolutionTime() {
        return getAverageResolutionTimeForStatus(IssueStatus.RESOLVED);
    }
}