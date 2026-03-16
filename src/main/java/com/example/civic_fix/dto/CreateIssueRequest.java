package com.example.civic_fix.dto;

import lombok.Data;

@Data
public class CreateIssueRequest {
    private String title;
    private String description;
    private Long categoryId;
    private Double latitude;
    private Double longitude;

    private String locationType;
}