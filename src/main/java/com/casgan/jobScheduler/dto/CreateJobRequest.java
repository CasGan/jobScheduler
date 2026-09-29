package com.casgan.jobScheduler.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CreateJobRequest {

    @NotBlank(message="Job name is required")
    @Size(max=100, message="Job name cannot exceed 100 characters")
    private String name; 


    @NotBlank(message="Job type is required") 
    private String jobType;
    
    @Size(max=500, message="Description cannot exceed 500 characters")
    private String description;

    @NotNull(message="Scheduled time is required")
    @Future(message="Scheduled time must be in the future")
    private LocalDateTime scheduledTime;

    public CreateJobRequest(){
    }

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
