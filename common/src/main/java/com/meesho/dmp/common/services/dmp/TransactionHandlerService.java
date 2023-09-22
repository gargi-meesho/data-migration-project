package com.meesho.dmp.common.services.dmp;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;

import java.util.List;

public interface TransactionHandlerService {
    List<ProductPriceDetailEntity> updateAndSaveAllPricingData(List<ProductPriceDetailEntity> entities);

    ProductPriceDetailEntity fetchProductPriceDetailsByIds(Long productId, Long supplierId);
}
