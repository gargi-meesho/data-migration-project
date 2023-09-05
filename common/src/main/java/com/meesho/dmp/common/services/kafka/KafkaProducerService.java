package com.meesho.dmp.common.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.dmp.common.config.kafka.KafkaTopicConfig;
import com.meesho.dmp.common.dto.CsvData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;


@Service
@Slf4j
public class KafkaProducerService {
    private static final String LOG_PREFIX = "[KafkaProducerService]";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;
    @Autowired
    private ObjectMapper objectMapper;

    private void produce(String topicName, String payload) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, payload);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                log.debug("{} Topic: {}, payload: {} delivery success", LOG_PREFIX, topicName,
                          stringStringSendResult.getRecordMetadata());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("{} Topic: {}, Error in sending payload: {}", LOG_PREFIX, topicName, payload, throwable);
            }
        });

    }

    public void produceCsvPricingData(List<CsvData> csvDataList) throws JsonProcessingException {
        produce(kafkaTopicConfig.getCsvPricingDataTopic(), objectMapper.writeValueAsString(csvDataList));
    }

}
