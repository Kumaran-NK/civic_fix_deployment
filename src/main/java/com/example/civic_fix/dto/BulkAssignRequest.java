package com.example.civic_fix.dto;
import java.util.*;
import lombok.Data;

@Data
public class BulkAssignRequest {
    private List<Long> issueId;
    private Long departmentId;
    private Long officerId;
}
