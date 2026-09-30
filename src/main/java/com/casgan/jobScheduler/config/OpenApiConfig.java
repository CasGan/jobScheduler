package com.casgan.jobScheduler.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Job Scheduler API",
        version = "1.0",
        description = "REST API for creating, scheduling, executing, " + "retrying, and auditing scheduled jobs."
    )
)

public class OpenApiConfig {
    
}
