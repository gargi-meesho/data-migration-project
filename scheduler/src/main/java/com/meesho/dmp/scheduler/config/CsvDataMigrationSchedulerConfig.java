package com.meesho.dmp.scheduler.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class CsvDataMigrationSchedulerConfig {

    private static final String LOG_PREFIX = "[CsvDataMigrationSchedulerConfig]";
    @Autowired
    private CsvDataSchedulerService csvDataSchedulerService;

    @Scheduled(cron = "${data-migration.cron-expression}")
    public void scheduledCsvApiProcessing() throws JsonProcessingException {
        log.info("{} scheduledCsvApiProcessing started", LOG_PREFIX);
        csvDataSchedulerService.fetchAndProcessDataFromCsv();
    }
}
