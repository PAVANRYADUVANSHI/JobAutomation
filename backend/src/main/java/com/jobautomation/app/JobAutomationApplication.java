package com.jobautomation.app;

import com.jobautomation.app.config.RenderDatasourceUrlFixer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobAutomationApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JobAutomationApplication.class);
        app.addInitializers(new RenderDatasourceUrlFixer());
        app.run(args);
    }
}
