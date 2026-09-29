package com.casgan.jobScheduler.dto;

import java.time.LocalDateTime;

import com.casgan.jobScheduler.model.ExecutionStatus;
import com.casgan.jobScheduler.model.JobExecutionHistory;

public class JobExecutionHistoryResponse {

    private Long id; 
    private Long jobId;
    private Integer attemptNumber;
    private ExecutionStatus status;
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private String errorMessage;

    public JobExecutionHistoryResponse() {
    }

    public static JobExecutionHistoryResponse fromEntity(
            JobExecutionHistory history
    ) {

        JobExecutionHistoryResponse response =
                new JobExecutionHistoryResponse();

        response.setId(history.getId());

        response.setJobId(
                history.getJob().getId()
        );

        response.setAttemptNumber(
                history.getAttemptNumber()
        );

        response.setStatus(
                history.getStatus()
        );

        response.setStartedAt(
                history.getStartedAt()
        );

        response.setCompletedAt(
                history.getCompletedAt()
        );

        response.setErrorMessage(
                history.getErrorMessage()
        );

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
