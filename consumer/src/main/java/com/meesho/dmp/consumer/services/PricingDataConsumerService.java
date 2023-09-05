package com.meesho.dmp.consumer.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.services.PricingDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meesho.dmp.common.constants.KafkaConstants.PRICING_DATA_GROUP_ID;
import static com.meesho.dmp.common.constants.KafkaConstants.PRICING_DATA_TOPIC_NAME;

@Service
@Slf4j
public class PricingDataConsumerService {

    private static final String LOG_PREFIX = "[PricingDataConsumerService]";
    @Autowired
    PricingDataService pricingDataProcessingService;
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = PRICING_DATA_TOPIC_NAME, groupId = PRICING_DATA_GROUP_ID)
    public void saveCsvPricingDataListener(String csvData) {

        try {
            log.info("{} saveCsvPricingDataListener data: {}", LOG_PREFIX, csvData);
            List<CsvData> csvDataList = objectMapper.readValue(csvData, new TypeReference<List<CsvData>>() {
            });
            pricingDataProcessingService.processAndSaveCsvPricingData(csvDataList);

        } catch (Exception e) {
            log.error("{} saveCsvPricingDataListener failed: {}", LOG_PREFIX, ExceptionUtils.getStackTrace(e));
        }
    }
}
