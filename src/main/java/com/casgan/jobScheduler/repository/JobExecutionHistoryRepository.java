package com.casgan.jobScheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.casgan.jobScheduler.model.JobExecutionHistory;

/*Gives us save(), findById(), findAll(), delete()  */

public interface JobExecutionHistoryRepository 
        extends JpaRepository<JobExecutionHistory, Long>{
    List<JobExecutionHistory>findByJobIdOrderByAttemptNumberAsc(Long jobId);
}
