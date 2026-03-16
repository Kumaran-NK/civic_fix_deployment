package com.example.civic_fix.dto;

import java.util.List;
import lombok.Data;

@Data
public class BulkAssignResult {
    private int successCount;
    private int failedCount;
    private List<BulkAssignItemResult> results;
}

