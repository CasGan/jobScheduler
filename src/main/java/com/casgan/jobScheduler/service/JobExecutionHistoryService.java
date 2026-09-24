package com.casgan.jobScheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.casgan.jobScheduler.model.ExecutionStatus;
import com.casgan.jobScheduler.model.JobExecutionHistory;
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.repository.JobExecutionHistoryRepository;

@Service
public class JobExecutionHistoryService {

    private final JobExecutionHistoryRepository repository;

    public JobExecutionHistoryService(
            JobExecutionHistoryRepository repository
    ) {
        this.repository = repository;
    }

    /*Creates a new history record when a job begins an execution attempt  */
    public JobExecutionHistory startExecution(
            ScheduledJob job,
            int attemptNumber
    ) {
        System.out.println(
                ">>> startExecution() entered"
        );

        System.out.println(
                ">>> History job ID: "
                + job.getId()
        );

        System.out.println(
                ">>> Attempt number: "
                + attemptNumber
        );
        JobExecutionHistory history = new JobExecutionHistory();
        history.setJob(job);
        history.setAttemptNumber(attemptNumber);
        history.setStatus(ExecutionStatus.RUNNING);
        history.setStartedAt(LocalDateTime.now());
        System.out.println(
                ">>> History object created"
        );

        System.out.println(
                ">>> About to call history repository.save()"
        );

        JobExecutionHistory saved
                = repository.save(history);

        System.out.println(
                ">>> repository.save() returned"
        );

        System.out.println(
                ">>> Saved history ID: "
                + saved.getId()
        );

        return saved;

    }

    public void markSuccess(
            JobExecutionHistory history
    ) {

        history.setStatus(
                ExecutionStatus.SUCCESS
        );

        history.setCompletedAt(
                LocalDateTime.now()
        );

        repository.save(history);
    }

    /*
     * Marks an individual execution attempt as failed.
     */
    public void markFailure(
            JobExecutionHistory history,
            String errorMessage
    ) {

        history.setStatus(
                ExecutionStatus.FAILED
        );

        history.setCompletedAt(
                LocalDateTime.now()
        );

        history.setErrorMessage(
                errorMessage
        );

        repository.save(history);
    }

    /* Returns all execution attempts for one scheduled job. */
    public List<JobExecutionHistory> getHistory(
            Long jobId
    ) {

        return repository
                .findByJobIdOrderByAttemptNumberAsc(jobId);
    }

}
