package com.meesho.dmp.scheduler.controller;

import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("api/v1/scheduler")
public class CsvDataMigrationController {

    @Autowired
    private CsvDataSchedulerService csvDataSchedulerService;

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerScheduler() {
        try {
            csvDataSchedulerService.fetchAndProcessDataFromCsv();
            return ResponseEntity.ok().body("Scheduler triggered successfully at " + ZonedDateTime.now());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to trigger scheduler: " + e.getMessage());
        }
    }
}
