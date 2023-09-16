package com.meesho.dmp.common.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meesho.dmp.common.dto.CsvData;

import java.util.List;


public interface KafkaProducerService {

    void produceCsvPricingData(List<CsvData> csvDataList) throws JsonProcessingException;

}
