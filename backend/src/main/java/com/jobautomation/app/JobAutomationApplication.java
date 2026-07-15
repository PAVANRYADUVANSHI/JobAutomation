package com.jobautomation.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class,
    org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration.class,
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@EnableScheduling
public class JobAutomationApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobAutomationApplication.class, args);
    }
}
