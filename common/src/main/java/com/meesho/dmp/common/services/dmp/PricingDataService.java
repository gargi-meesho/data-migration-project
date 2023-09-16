package com.meesho.dmp.common.services.dmp;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;

import java.util.List;


public interface PricingDataService {
    void processAndSaveCsvPricingData(List<CsvData> csvDataList);

    ProductPriceDetailEntity fetchProductPriceDetails(Long productId, Long supplierId);
}
