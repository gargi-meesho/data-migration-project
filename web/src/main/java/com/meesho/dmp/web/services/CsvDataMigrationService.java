package com.meesho.dmp.web.services;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;

import java.util.List;

public interface CsvDataMigrationService {
    void processAndSaveAllData(List<CsvData> csvDataList);

    ProductPriceDetailEntity getProductPriceDetails(Long productId, Long supplierId);
}
