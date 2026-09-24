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
    public ScheduledJob createJob( @Valid @RequestBody CreateJobRequest request){
        return service.createJob(request);
    }

    @GetMapping
    public List<ScheduledJob> getJobs(){
        return service.getAllJobs();
    }

    @GetMapping("/{id}")
    public ScheduledJob getJob(@PathVariable Long id){
        return service.getJob(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable Long id){
        service.deleteJob(id);
    }

    @GetMapping("/{id}/history")
    public List<JobExecutionHistory> getJobHistory(@PathVariable Long id){
        service.getJob(id);

        return historyService.getHistory(id);
    }
    
}
