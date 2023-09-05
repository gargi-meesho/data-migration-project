package com.meesho.dmp.scheduler.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CsvDataSchedulerService {

    void fetchAndProcessDataFromCsv() throws JsonProcessingException;
}
