package com.casgan.jobScheduler.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.service.ScheduledJobService; 

@Component
public class JobScheduler {

    private static final Logger log = LoggerFactory.getLogger( JobScheduler.class);

    private final ScheduledJobService service; 

    public JobScheduler(ScheduledJobService service){
        this.service = service; 
    }

    @Scheduled(fixedRate = 5000)
    public void checkForJobs(){

        List<ScheduledJob> jobs = service.findReadyJobs();

        log.debug("Scheduler scan completed readyJobs={}", jobs.size());

        for(ScheduledJob job: jobs){

            log.info("Scheduler selected job id={} scheduledTime={} nextRunAt={} ", job.getId(), job.getScheduledTime(), job.getNextRunAt());

            service.executeJob(job);
        }
    }
}
