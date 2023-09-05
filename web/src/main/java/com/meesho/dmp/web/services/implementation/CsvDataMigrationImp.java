package com.meesho.dmp.web.services.implementation;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.services.PricingDataService;
import com.meesho.dmp.web.services.CsvDataMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class CsvDataMigrationImp implements CsvDataMigrationService {

    @Autowired
    PricingDataService pricingDataProcessingService;

    @Override
    public void processAndSaveAllData(List<CsvData> csvDataList) {
        pricingDataProcessingService.processAndSaveCsvPricingData(csvDataList);
    }

    @Override
    public ProductPriceDetailEntity getProductPriceDetails(Long productId, Long supplierId) {
        return pricingDataProcessingService.fetchProductPriceDetails(productId, supplierId);
    }

}
