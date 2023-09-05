package com.meesho.dmp.common.services;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.repository.ProductPriceDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class TransactionHandlerService {

    private static final String LOG_PREFIX = "[TransactionHandlerService]";

    @Value("${spring.cache.redis.time-to-live-hours}")
    int cacheTtlHours;

    @Autowired
    private ProductPriceDetailRepository productPriceDetailRepository;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Transactional
    public void updateAndSaveAllPricingData(List<ProductPriceDetailEntity> entities) {


        for (ProductPriceDetailEntity entity : entities) {
            String uniqueKey = getUniqueKey(entity);

            Long existingEntityId = redisTemplate.opsForValue().get(uniqueKey);

            if (existingEntityId != null) {
                entity.setId(existingEntityId);
            }
        }

        List<ProductPriceDetailEntity> savedEntities = productPriceDetailRepository.saveAll(entities);

        for (ProductPriceDetailEntity savedEntity : savedEntities) {
            redisTemplate.opsForValue()
                         .set(getUniqueKey(savedEntity), savedEntity.getId(), cacheTtlHours, TimeUnit.HOURS);
        }

        log.info("{} updateAndSaveAllPricingData saved: {}", LOG_PREFIX, savedEntities);
    }

    public ProductPriceDetailEntity fetchProductPriceDetailsByIds(Long productId, Long supplierId) {
        return productPriceDetailRepository.findByProductIdAndSupplierId(productId, supplierId);
    }

    private String getUniqueKey(ProductPriceDetailEntity entity) {
        return entity.getProductId() + "_" + entity.getSupplierId();
    }

}
