package com.example.civic_fix.Entity;

public enum IssueStatus {
    REPORTED("Reported", "Issue has been reported and pending review"),
    ASSIGNED("Assigned", "Issue has been assigned to a department/officer"),
    IN_PROGRESS("In Progress", "Work has started on this issue"),
    RESOLVED("Resolved", "Issue has been resolved"),
    CLOSED("Closed", "Issue is closed "),
    REOPENED("Reopened", "Issue has been reopened"),
    REJECTED("Rejected", "Issue has been rejected");
    
    private final String displayName;
    private final String description;
    
    IssueStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canTransitionTo(IssueStatus newStatus) {
        switch(this) {
            case REPORTED:
                return newStatus == ASSIGNED || newStatus == REJECTED;
            case ASSIGNED:
                return newStatus == IN_PROGRESS || newStatus == REJECTED;
            case IN_PROGRESS:
                return newStatus == RESOLVED || newStatus == REOPENED;
            case RESOLVED:
                return newStatus == CLOSED || newStatus == REOPENED;
            case REOPENED:
                return newStatus == ASSIGNED || newStatus == IN_PROGRESS;
            case REJECTED:
                return newStatus == REPORTED || newStatus == CLOSED;
            case CLOSED:
                return false; 
            default:
                return false;
        }
    }
}