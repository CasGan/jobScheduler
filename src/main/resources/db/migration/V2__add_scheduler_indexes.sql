CREATE INDEX idx_scheduled_jobs_status
ON scheduled_jobs(status);


CREATE INDEX idx_scheduled_jobs_scheduled_time
ON scheduled_jobs(scheduled_time);


CREATE INDEX idx_scheduled_jobs_next_run_at
ON scheduled_jobs(next_run_at);


CREATE INDEX idx_job_execution_history_job_id
ON job_execution_history(job_id);