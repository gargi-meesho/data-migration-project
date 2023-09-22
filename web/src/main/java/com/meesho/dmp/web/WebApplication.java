package com.meesho.dmp.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.meesho.dmp.common", "com.meesho.dmp.web"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ,
                pattern = {
                        "com.meesho.dmp.common.config.kafka..*",
                        "com.meesho.dmp.common.services.kafka..*"
                }
        )
)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
