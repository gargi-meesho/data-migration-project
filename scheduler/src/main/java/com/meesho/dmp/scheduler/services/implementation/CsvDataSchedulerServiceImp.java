package com.meesho.dmp.scheduler.services.implementation;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.exceptions.CsvReadingException;
import com.meesho.dmp.common.models.ApiResponse;
import com.meesho.dmp.common.models.CsvDataPostRequest;
import com.meesho.dmp.common.services.kafka.KafkaProducerService;
import com.meesho.dmp.common.services.webIntegration.WebIntegrationService;
import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CsvDataSchedulerServiceImp implements CsvDataSchedulerService {

    private static final String LOG_PREFIX = "[CsvDataSchedulerServiceImp]";

    @Value("${data-migration.csv-file-path}")
    private String csvFilePath;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private WebIntegrationService webIntegrationService;

    @Override
    public void fetchAndProcessDataFromCsv() {

        List<CsvData> csvDataList = null;

        try {
            csvDataList = readAllDataFromCsv();
            ResponseEntity<ApiResponse> response = postDataToWebApi(csvDataList);

            log.info("{} postDataToWebApi success:{}", LOG_PREFIX, response.getBody());

        } catch (CsvReadingException e) {
            log.error("{} readAllDataFromCsv failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));

        } catch (HttpServerErrorException e) {
            log.error("{} postDataToWebApi server error: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
            produceDataToKafkaTopic(csvDataList);

        } catch (Exception e) {
            log.error("{} fetchAndProcessDataFromCsv failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));

        }

    }

    private List<CsvData> readAllDataFromCsv() {

        log.info("{} readAllDataFromCsv: {}", LOG_PREFIX, csvFilePath);

        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.schemaFor(CsvData.class)
                                           .withHeader();
            File csvFile = new File(csvFilePath);

            MappingIterator<CsvData> mappingIterator = csvMapper
                    .readerFor(CsvData.class)
                    .with(csvSchema)
                    .readValues(csvFile);

            return mappingIterator.readAll();

        } catch (IOException e) {
            String errorMessage = "Error reading CSV file: " + e.getMessage();
            throw new CsvReadingException(errorMessage, e);
        }

    }

    private ResponseEntity<ApiResponse> postDataToWebApi(List<CsvData> csvDataList) {
        log.info("{} postDataToWebApi data: {}", LOG_PREFIX, csvDataList);

        CsvDataPostRequest requestBody = new CsvDataPostRequest(csvDataList);
        return webIntegrationService.sendCsvPostRequest(requestBody);
    }

    private void produceDataToKafkaTopic(List<CsvData> csvDataList) {
        try {
            log.info("{} produceDataToKafkaTopic data: {}", LOG_PREFIX, csvDataList);
            kafkaProducerService.produceCsvPricingData(csvDataList);

        } catch (Exception e) {
            log.error("{} produceDataToKafkaTopic failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
        }
    }
}
