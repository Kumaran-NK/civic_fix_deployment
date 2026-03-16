package com.example.civic_fix.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResolveIssueRequest {
    private String resolutionNotes;
    private List<String> attachments;
}