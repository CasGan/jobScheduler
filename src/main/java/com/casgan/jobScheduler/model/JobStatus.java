package com.casgan.jobScheduler.model;

public enum JobStatus {
    PENDING,
    RUNNING,
    RETRY_WAIT,
    COMPLETED,
    FAILED,
    CANCELLED
}
