package com.meesho.dmp.web.services;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.models.response.ProductPriceDetailGetResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CsvDataMigrationService {
    ResponseEntity<CsvDataPostResponse> processAndSaveAllData(List<CsvData> csvDataList);

    ResponseEntity<ProductPriceDetailGetResponse> getProductPriceDetails(Long productId, Long supplierId);
}
