package com.casgan.jobScheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.casgan.jobScheduler.dto.CreateJobRequest;
import com.casgan.jobScheduler.model.JobExecutionHistory;
import com.casgan.jobScheduler.model.JobStatus;
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.repository.ScheduledJobRepository;

@Service
public class ScheduledJobService {

    private final ScheduledJobRepository repository;
    private final JobExecutionHistoryService historyService;

    public ScheduledJobService(ScheduledJobRepository repository, JobExecutionHistoryService historyService) {
        this.repository = repository;
        this.historyService = historyService;
    }

    public ScheduledJob createJob(CreateJobRequest request) {

        ScheduledJob job = new ScheduledJob(
                request.getName(),
                request.getDescription(),
                request.getJobType(),
                request.getScheduledTime()
        );
        return repository.save(job);
    }

    public List<ScheduledJob> getAllJobs() {
        return repository.findAll();
    }

    public ScheduledJob getJob(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Job not found: " + id));
    }

    public void deleteJob(Long id) {
        repository.deleteById(id);
    }

    public List<ScheduledJob> findReadyJobs() {
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Application current time: " + now);

        return repository.findJobsReadyToRun(JobStatus.PENDING, now);
    }

    public void executeJob(ScheduledJob job) {
        int attemptNumber = job.getRetryCount() + 1;

        JobExecutionHistory history = historyService.startExecution(job, attemptNumber);

        try {
            // Job officially begins execution attempt
            job.setStatus(JobStatus.RUNNING);

            if (job.getExecutedAt() == null) {
                job.setExecutedAt(LocalDateTime.now());
            }
            // reattempting, removes last error
            job.setLastError(null);

            repository.save(job);
            //  performs work 
            performJob(job);

            // if performJob() doesn't throw exception
            // the job succeeded
            job.setStatus(JobStatus.COMPLETED);

            job.setCompletedAt(LocalDateTime.now());

            job.setNextRunAt(null);

            repository.save(job);

            historyService.markSuccess(history);

        } catch (Exception exception) {
            handleFailure(job, history, exception); 
        }

    }
    
private void performJob(
        ScheduledJob job
) throws Exception {

    System.out.println(
            "Executing job ID "
                    + job.getId()
                    + ": "
                    + job.getName()
    );

    Thread.sleep(2000);

    if ("FAIL_TEST".equals(job.getJobType())) {

        throw new RuntimeException(
                "Intentional test failure"
        );
    }

    if ("RETRY_TEST".equals(job.getJobType())
            && job.getRetryCount() < 2) {

        throw new RuntimeException(
                "Temporary failure"
        );
    }
}

   private void handleFailure(
        ScheduledJob job,
        JobExecutionHistory history,
        Exception exception
) {

    System.out.println("===== HANDLE FAILURE =====");

    System.out.println(
            "Job ID: " + job.getId()
    );

    System.out.println(
            "Error: " + exception.getMessage()
    );

    System.out.println(
            "Retry count BEFORE: "
                    + job.getRetryCount()
    );

    String errorMessage =
            exception.getMessage();

    job.setLastError(errorMessage);

    historyService.markFailure(
            history,
            errorMessage
    );

    if (job.getRetryCount()
            < job.getMaxRetries()) {

        int newRetryCount =
                job.getRetryCount() + 1;

        job.setRetryCount(newRetryCount);

        job.setStatus(JobStatus.PENDING);

        job.setNextRunAt(
                LocalDateTime.now()
                        .plusSeconds(30)
        );

        System.out.println(
                "Retry count AFTER: "
                        + job.getRetryCount()
        );

        System.out.println(
                "Next run: "
                        + job.getNextRunAt()
        );

    } else {

        job.setStatus(JobStatus.FAILED);
        job.setNextRunAt(null);

        System.out.println(
                "No retries remaining."
        );
    }

    repository.save(job);

    System.out.println(
            "Job saved with status: "
                    + job.getStatus()
    );

    System.out.println("==========================");
}
    
}


