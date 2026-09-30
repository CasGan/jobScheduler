package com.casgan.jobScheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.casgan.jobScheduler.dto.CreateJobRequest;
import com.casgan.jobScheduler.exception.JobNotFoundException;
import com.casgan.jobScheduler.model.JobExecutionHistory;
import com.casgan.jobScheduler.model.JobStatus;
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.repository.ScheduledJobRepository;

@Service
public class ScheduledJobService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledJobService.class);

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
        return repository.findById(id).orElseThrow(() -> new JobNotFoundException(id));
    }

    public void deleteJob(Long id) {
        ScheduledJob job = getJob(id);
        
        repository.delete(job);
    }

    public List<ScheduledJob> findReadyJobs() {
        LocalDateTime now = LocalDateTime.now();

        return repository.findJobsReadyToRun(JobStatus.PENDING, now);
    }

    public void executeJob(ScheduledJob job) {
        int attemptNumber = job.getRetryCount() + 1;

        log.info("Starting job execution id={} attempt={}", job.getId(), attemptNumber);

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

            log.debug("Job id={} marked RUNNING", job.getId());
            //  performs work 
            performJob(job);

            // if performJob() doesn't throw exception
            // the job succeeded
            job.setStatus(JobStatus.COMPLETED);

            job.setCompletedAt(LocalDateTime.now());

            job.setNextRunAt(null);

            repository.save(job);

            historyService.markSuccess(history);
            
            log.info("Job completed successfully id={} attempt={}", job.getId(), attemptNumber);

        } catch (Exception exception) {
            handleFailure(job, history, exception);
        }

    }

    private void performJob(
            ScheduledJob job
    ) throws Exception {

        log.debug("Performing job id={} type={}", job.getId(), job.getJobType());

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

        String errorMessage = exception.getMessage();

        job.setLastError(errorMessage);

        historyService.markFailure(
                history,
                errorMessage
        );

        log.warn("Job Execution Failed id={} retryCount={} maxRetries={} error={}", job.getId(), job.getRetryCount(), job.getMaxRetries(), errorMessage);

        if (job.getRetryCount() < job.getMaxRetries()) {

            int newRetryCount = job.getRetryCount() + 1;

            job.setRetryCount(newRetryCount);

            job.setStatus(JobStatus.PENDING);

            job.setNextRunAt(LocalDateTime.now().plusSeconds(30));

            log.info("Job id={} scheduled for retry={} nextRunAt={}", job.getId(), newRetryCount, job.getNextRunAt());

        } else {

            job.setStatus(JobStatus.FAILED);
            job.setNextRunAt(null);

            log.error("Job permanently failed id={} after {} retries", job.getId(), job.getRetryCount(), exception);
        }

        repository.save(job);
    }

}
