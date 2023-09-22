package com.meesho.dmp.common.services.dmp.implementation;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.repository.ProductPriceDetailRepository;
import com.meesho.dmp.common.services.dmp.TransactionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional
public class TransactionHandlerServiceImp implements TransactionHandlerService {

    @Autowired
    private ProductPriceDetailRepository productPriceDetailRepository;

    public List<ProductPriceDetailEntity> updateAndSaveAllPricingData(List<ProductPriceDetailEntity> entities) {
        return productPriceDetailRepository.saveAll(entities);
    }

    public ProductPriceDetailEntity fetchProductPriceDetailsByIds(Long productId, Long supplierId) {
        return productPriceDetailRepository.findByProductIdAndSupplierId(productId, supplierId);
    }

}
