// Object represents what clients are allowed to see

package com.casgan.jobScheduler.dto;

import java.time.LocalDateTime;

import com.casgan.jobScheduler.model.JobStatus;
import com.casgan.jobScheduler.model.ScheduledJob;

public class ScheduledJobResponse {
    private Long id;
    private String name; 
    private String description; 
    private String jobType;
    private JobStatus status;

    private LocalDateTime scheduledTime;
    private LocalDateTime nextRunAt;

    private Integer retryCount;
    private Integer maxRetries;
    
    private String lastError;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime executedAt;
    private LocalDateTime completedAt;
    

    public ScheduledJobResponse(){
    }

    public static ScheduledJobResponse fromEntity(ScheduledJob job){
        if(job == null){ 
            return null; 
        }

        ScheduledJobResponse response = new ScheduledJobResponse(); 

        response.setId(job.getId());
        response.setName(job.getName());
        response.setDescription(job.getDescription());
        response.setJobType(job.getJobType());
        response.setStatus(job.getStatus());
        response.setScheduledTime(job.getScheduledTime());
        response.setNextRunAt(job.getNextRunAt());
        response.setRetryCount(job.getRetryCount());
        response.setMaxRetries(job.getMaxRetries());
        response.setLastError(job.getLastError());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());
        response.setExecutedAt(job.getExecutedAt());
        response.setCompletedAt(job.getCompletedAt());

        return response; 

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(LocalDateTime nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
}
