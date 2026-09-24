package com.casgan.jobScheduler.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateJobRequest {
    @NotBlank
    private String name; 
    @NotBlank 
    private String jobType;
    
    private String description;

    @NotNull
    @Future
    private LocalDateTime scheduledTime;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name; 
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description; 
    }

    public LocalDateTime getScheduledTime(){
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime){
        this.scheduledTime = scheduledTime; 
    }

    public String getJobType(){
        return jobType;
    }

    public void setJobType(String jobType){
        this.jobType = jobType; 
    }
}
