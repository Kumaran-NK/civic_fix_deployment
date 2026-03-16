package com.example.civic_fix.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.civic_fix.Entity.IssueUpdate;

@Repository
public interface IssueUpdateRepository extends JpaRepository<IssueUpdate, Long> {
    List<IssueUpdate> findByIssue_IssueId(Long issueId);
}