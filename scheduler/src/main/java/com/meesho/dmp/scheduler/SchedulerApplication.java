package com.meesho.dmp.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        exclude = DataSourceAutoConfiguration.class
)
@ComponentScan(
        basePackages = {
                "com.meesho.dmp.common.services.webIntegration",
                "com.meesho.dmp.common.services.kafka",
                "com.meesho.dmp.common.config.kafka",
                "com.meesho.dmp.common.exceptions",
                "com.meesho.dmp.scheduler"
        }
)
public class SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

}
