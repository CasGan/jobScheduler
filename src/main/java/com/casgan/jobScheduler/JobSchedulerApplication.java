/* A client sends an HTTP request to Spring Boot
   The controller receives that request and with Spring's help gets the request data into Java Objects
   The controller delegates the operation to the service.
   The Service contains the app's business logic and determines what should happen.
   If it needs to interact with persistent data, it uses the Repository.
   The Repository uses JPA/Hibernate to communicate with the database. 
   Entities represent the persistent data that JPA maps between Java Objects and database rows. 
   The result travels back through the layers and eventually becomes an HTTP response. 
 */

package com.casgan.jobScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSchedulerApplication.class, args);
	}

}
