package com.casgan.jobScheduler.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(Long id){
        super("Scheduled job not found with id: " + id);
    }
}
