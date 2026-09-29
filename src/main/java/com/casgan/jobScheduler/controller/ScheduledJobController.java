package com.casgan.jobScheduler.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casgan.jobScheduler.dto.CreateJobRequest;
import com.casgan.jobScheduler.dto.JobExecutionHistoryResponse;
import com.casgan.jobScheduler.dto.ScheduledJobResponse;
import com.casgan.jobScheduler.model.JobExecutionHistory;
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.service.JobExecutionHistoryService;
import com.casgan.jobScheduler.service.ScheduledJobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
public class ScheduledJobController {

    private final ScheduledJobService service;
    private final JobExecutionHistoryService historyService;

    public ScheduledJobController(ScheduledJobService service, JobExecutionHistoryService historyService){
        this.service = service; 
        this.historyService = historyService; 
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduledJobResponse createJob( @Valid @RequestBody CreateJobRequest request){
        ScheduledJob job = service.createJob(request);

        return ScheduledJobResponse.fromEntity(job);
    }

    @GetMapping
    public List<ScheduledJobResponse> getJobs(){
        List<ScheduledJob> jobs = service.getAllJobs();

        return jobs.stream().map(ScheduledJobResponse::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ScheduledJobResponse getJob(@PathVariable Long id){
        ScheduledJob job = service.getJob(id);

        return ScheduledJobResponse.fromEntity(job);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable Long id){
        service.deleteJob(id);
    }

    @GetMapping("/{id}/history")
    public List<JobExecutionHistoryResponse> getJobHistory(@PathVariable Long id){
        service.getJob(id);

        List<JobExecutionHistory> history = historyService.getHistory(id);

        return history.stream().map(JobExecutionHistoryResponse::fromEntity).toList();
    }
    
}
