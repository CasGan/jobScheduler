package com.casgan.jobScheduler.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.service.ScheduledJobService; 

@Component
public class JobScheduler {
    private final ScheduledJobService service; 

    public JobScheduler(ScheduledJobService service){
        this.service = service; 
    }

    @Scheduled(fixedRate = 5000)
    public void checkForJobs(){

        System.out.println("========== SCHEDULER CHECK========== ");
        System.out.println("Current Java time: " + LocalDateTime.now());

        List<ScheduledJob> jobs = service.findReadyJobs();

        System.out.println("Ready jobs found: " + jobs.size() );

        for(ScheduledJob job: jobs){

             System.out.println(
                "READY JOB -> ID: " + job.getId()
                        + ", status: " + job.getStatus()
                        + ", scheduledTime: " + job.getScheduledTime()
                        + ", nextRunAt: " + job.getNextRunAt()
                    );
            service.executeJob(job);
        }
    }
}
