package com.meesho.dmp.scheduler.cron;

import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class CsvDataMigrationScheduler {

    private static final String LOG_PREFIX = "[CsvDataMigrationScheduler]";

    @Autowired
    private CsvDataSchedulerService csvDataSchedulerService;

    @Scheduled(cron = "${data-migration.cron-expression}")
    public void processCsvDataMigration() {
        log.info("{} scheduledCsvApiProcessing started", LOG_PREFIX);
        try {
            csvDataSchedulerService.fetchAndProcessDataFromCsv();
        } catch (Exception e) {
            log.error("{} processCsvDataMigration cron failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
        }
    }

}