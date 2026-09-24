package com.casgan.jobScheduler.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.casgan.jobScheduler.model.JobStatus;
import com.casgan.jobScheduler.model.ScheduledJob;

public interface ScheduledJobRepository
        //  JpaRepo allows us to use: save(), findAll(), findById(), deleteById(), existsById() 
        extends JpaRepository<ScheduledJob, Long> {

    @Query("""
        SELECT j
        FROM ScheduledJob j
        WHERE j.status = :status
        AND (
            (
                j.nextRunAt IS NULL
                AND j.scheduledTime <= :now
            )
            OR
            (
                j.nextRunAt IS NOT NULL
                AND j.nextRunAt <= :now
            )
        )
        """)
    List<ScheduledJob> findJobsReadyToRun(
            @Param("status") JobStatus status,
            @Param("now") LocalDateTime now
    );
}
