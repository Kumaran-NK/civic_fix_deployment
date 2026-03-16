package com.example.civic_fix.dto;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class SystemStatisticsDTO {
    private long totalUsers;
    private long totalCitizens;
    private long totalOfficers;
    private long totalAdmins;
    private long totalIssues;
    private Map<String, Long> issuesByStatus;
    private List<DepartmentIssueCountDTO> issuesByDepartment;
    private String averageResolutionTime;
    private long issuesReportedToday;
    private long issuesResolvedToday;
}

