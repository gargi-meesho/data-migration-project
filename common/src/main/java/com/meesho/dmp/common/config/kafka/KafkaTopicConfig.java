package com.meesho.dmp.common.config.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KafkaTopicConfig {

    @Value("${kafka-topic.csv-pricing-data-topic}")
    private String csvPricingDataTopic;
}