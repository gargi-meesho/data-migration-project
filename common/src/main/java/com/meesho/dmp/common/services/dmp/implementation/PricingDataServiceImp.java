package com.meesho.dmp.common.services.dmp.implementation;


import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.services.dmp.PricingDataService;
import com.meesho.dmp.common.services.dmp.TransactionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PricingDataServiceImp implements PricingDataService {

    private static final String LOG_PREFIX = "[PricingDataService]";

    @Value("${spring.cache.redis.time-to-live-hours}")
    int cacheTtlHours;

    @Autowired
    private TransactionHandlerService transactionHandlerService;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    private List<ProductPriceDetailEntity> convertDtoToEntities(List<CsvData> csvDataList) {

        return csvDataList.stream()
                          .map(csvData -> ProductPriceDetailEntity
                                  .builder()
                                  .refId(csvData.getId())
                                  .productId(csvData.getProductId())
                                  .supplierId(csvData.getSupplierId())
                                  .wdrpRecommendedPrice(csvData.getWdrpRecommendedPrice())
                                  .recommendedPrice(csvData.getRecommendedPrice())
                                  .build()
                              )
                          .collect(Collectors.toList());
    }

    @Override
    public void processAndSaveCsvPricingData(List<CsvData> csvDataList) {

        List<ProductPriceDetailEntity> entities = convertDtoToEntities(csvDataList);
        for (ProductPriceDetailEntity entity : entities) {
            String uniqueKey = getUniqueKey(entity);

            Long existingEntityId = redisTemplate.opsForValue().get(uniqueKey);

            if (existingEntityId != null) {
                entity.setId(existingEntityId);
            }
        }
        List<ProductPriceDetailEntity> savedEntities = transactionHandlerService.updateAndSaveAllPricingData(entities);

        for (ProductPriceDetailEntity savedEntity : savedEntities) {
            redisTemplate.opsForValue()
                         .set(getUniqueKey(savedEntity), savedEntity.getId(), cacheTtlHours, TimeUnit.HOURS);
        }

        log.info("{} updateAndSaveAllPricingData saved: {}", LOG_PREFIX, savedEntities);

    }

    @Override
    public ProductPriceDetailEntity fetchProductPriceDetails(Long productId, Long supplierId) {
        return transactionHandlerService.fetchProductPriceDetailsByIds(productId, supplierId);
    }

    private String getUniqueKey(ProductPriceDetailEntity entity) {
        return entity.getProductId() + "_" + entity.getSupplierId();
    }
}
