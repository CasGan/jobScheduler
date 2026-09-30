package com.casgan.jobScheduler.service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.casgan.jobScheduler.model.JobExecutionHistory;
import com.casgan.jobScheduler.model.JobStatus;
import com.casgan.jobScheduler.model.ScheduledJob;
import com.casgan.jobScheduler.repository.ScheduledJobRepository;

@ExtendWith(MockitoExtension.class)
class ScheduledJobServiceTest {

    @Mock
    private ScheduledJobRepository repository;

    @Mock
    private JobExecutionHistoryService historyService;

    private ScheduledJobService service;

    private JobExecutionHistory history;

    @BeforeEach
    void setUp() {
        service = new ScheduledJobService(
                repository,
                historyService
        );

        history = new JobExecutionHistory();

        when(repository.save(any(ScheduledJob.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void executeJob_shouldCompleteSuccessfulJob() {

        ScheduledJob job = createJob(
                "Success Test",
                "Should succeed",
                "TEST"
        );

        when(historyService.startExecution(job, 1))
                .thenReturn(history);

        service.executeJob(job);

        assertEquals(JobStatus.FAILED, job.getStatus());
        assertNotNull(job.getExecutedAt());
        assertNotNull(job.getCompletedAt());
        assertNull(job.getNextRunAt());
        assertNull(job.getLastError());

        verify(historyService).markSuccess(history);
    }

    @Test
    void executeJob_shouldScheduleRetryWhenExecutionFails() {

        ScheduledJob job = createJob(
                "Failure Test",
                "Should fail",
                "FAIL_TEST"
        );

        when(historyService.startExecution(job, 1))
                .thenReturn(history);

        service.executeJob(job);

        assertEquals(JobStatus.PENDING, job.getStatus());
        assertEquals(1, job.getRetryCount());
        assertEquals(
                "Intentional test failure",
                job.getLastError()
        );

        assertNotNull(job.getNextRunAt());
        assertNotNull(job.getExecutedAt());
        assertNull(job.getCompletedAt());

        verify(historyService).markFailure(
                history,
                "Intentional test failure"
        );
    }

    @Test
    void executeJob_shouldPermanentlyFailWhenRetriesAreExhausted() {

        ScheduledJob job = createJob(
                "Permanent Failure",
                "Should exhaust retries",
                "FAIL_TEST"
        );

        job.setRetryCount(3);
        job.setMaxRetries(3);

        when(historyService.startExecution(job, 4))
                .thenReturn(history);

        service.executeJob(job);

        assertEquals(JobStatus.FAILED, job.getStatus());
        
        assertEquals(3, job.getRetryCount());

        assertNull(job.getNextRunAt());
        assertNull(job.getCompletedAt());

        assertEquals(
                "Intentional test failure",
                job.getLastError()
        );

        verify(historyService).markFailure(
                history,
                "Intentional test failure"
        );
    }

    @Test
    void executeJob_shouldSucceedAfterTemporaryFailures() {

        ScheduledJob job = createJob(
                "Recovery Test",
                "Should eventually succeed",
                "RETRY_TEST"
        );

        job.setRetryCount(2);

        when(historyService.startExecution(job, 3))
                .thenReturn(history);

        service.executeJob(job);

        assertEquals(JobStatus.COMPLETED, job.getStatus());
        assertEquals(2, job.getRetryCount());

        assertNotNull(job.getExecutedAt());
        assertNotNull(job.getCompletedAt());

        assertNull(job.getNextRunAt());
        assertNull(job.getLastError());

        verify(historyService).markSuccess(history);
    }

    private ScheduledJob createJob(
            String name,
            String description,
            String jobType
    ) {
        return new ScheduledJob(
                name,
                description,
                jobType,
                LocalDateTime.now()
        );
    }
}