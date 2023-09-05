package com.meesho.dmp.common.config.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Getter
public class KafkaTopicConfig {

    @Value("${kafka-topic.csv-pricing-data-topic}")
    private String csvPricingDataTopic;
}