package org;

import lombok.extern.log4j.Log4j2;
import org.shortner.repository.CleanUpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@SpringBootApplication
@ComponentScan({"org.data", "org.shortner"})
@Log4j2
public class ScheduledCleanUpApplication {


    public static void main(String[] args) {
        SpringApplication.run(ScheduledCleanUpApplication.class, args);
    }
}
