package com.meesho.dmp.scheduler.controller;

import com.meesho.dmp.common.models.ApiResponse;
import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.meesho.dmp.common.utils.CommonUtils.createApiResponse;

@RestController
@RequestMapping("api/v1/scheduler")
public class CsvDataMigrationController {

    @Autowired
    private CsvDataSchedulerService csvDataSchedulerService;

    @PostMapping("/trigger")
    public ResponseEntity<ApiResponse<Void>> triggerScheduler() {
        try {
            csvDataSchedulerService.fetchAndProcessDataFromCsv();

            return createApiResponse(true, HttpStatus.OK, "Scheduler triggered successfully");

        } catch (Exception e) {
            return createApiResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to trigger scheduler: " + e.getMessage());
        }
    }
}
