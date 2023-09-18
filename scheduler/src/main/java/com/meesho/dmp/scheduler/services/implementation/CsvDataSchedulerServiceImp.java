package com.meesho.dmp.scheduler.services.implementation;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.exceptions.CsvReadingException;
import com.meesho.dmp.common.models.request.CsvDataPostRequest;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.services.kafka.KafkaProducerService;
import com.meesho.dmp.common.services.webIntegration.WebIntegrationService;
import com.meesho.dmp.scheduler.services.CsvDataSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.meesho.dmp.scheduler.constants.Constants.CSV_BATCH_SIZE;

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

        try {
            readAllDataFromCsvInBatch();
        } catch (Exception e) {
            log.error("{} fetchAndProcessDataFromCsv failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
        }

    }

    private void readAllDataFromCsvInBatch() {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.schemaFor(CsvData.class)
                                           .withHeader();
            File csvFile = new File(csvFilePath);

            MappingIterator<CsvData> mappingIterator = csvMapper
                    .readerFor(CsvData.class)
                    .with(csvSchema)
                    .readValues(csvFile);

            List<CsvData> csvDataList = new ArrayList<>();
            Long processedRecordCount = 0L;

            while (mappingIterator.hasNext()) {
                try {
                    CsvData csvData = mappingIterator.next();
                    csvDataList.add(csvData);
                    processedRecordCount++;

                } catch (Exception e) {
                    log.error("{} readAllDataFromCsvInBatch error in processing record no: {}", LOG_PREFIX,
                              processedRecordCount);
                }

                if (csvDataList.size() == CSV_BATCH_SIZE || !mappingIterator.hasNext()) {
                    processCsvDataInBatch(csvDataList);
                    csvDataList.clear();
                }
            }

        } catch (Exception e) {
            String errorMessage = "Error reading CSV file: " + ExceptionUtils.getStackTrace(e);
            throw new CsvReadingException(errorMessage, e);
        }
    }

    private void processCsvDataInBatch(List<CsvData> csvDataList) {
        log.info("{} processCsvDataInBatch records: {}", LOG_PREFIX, csvDataList);

        try {
            CsvDataPostResponse responseBody = postDataToWebApi(csvDataList);
            log.info("{} postDataToWebApi success: {}", LOG_PREFIX, responseBody);

        } catch (Exception e) {
            log.error("{} postDataToWebApi failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
            produceDataToKafkaTopic(csvDataList);
        }
    }

    private CsvDataPostResponse postDataToWebApi(List<CsvData> csvDataList) {
        log.info("{} postDataToWebApi data: {}", LOG_PREFIX, csvDataList);

        CsvDataPostRequest requestBody = new CsvDataPostRequest(csvDataList);
        ResponseEntity<CsvDataPostResponse> response = webIntegrationService.sendCsvPostRequest(requestBody);

        return response.getBody();
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
