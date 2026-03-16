package com.example.civic_fix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.civic_fix.Entity.IssueCategory;

@Repository
public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Long>
{
    boolean existsByName(String name);
}
