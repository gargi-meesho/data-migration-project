package com.meesho.dmp.web.services.implementation;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.exceptions.NoRecordFoundException;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.models.response.ProductPriceDetailGetResponse;
import com.meesho.dmp.common.services.dmp.PricingDataService;
import com.meesho.dmp.web.services.CsvDataMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.meesho.dmp.common.utils.CommonUtils.createCsvDataPostResponse;
import static com.meesho.dmp.common.utils.CommonUtils.createProductPriceDetailGetResponse;


@Service
@Slf4j
public class CsvDataMigrationImp implements CsvDataMigrationService {

    @Autowired
    PricingDataService pricingDataProcessingService;

    @Override
    public ResponseEntity<CsvDataPostResponse> processAndSaveAllData(List<CsvData> csvDataList) {
        if (CollectionUtils.isEmpty(csvDataList)) {
            throw new IllegalArgumentException("List of CSV data must be non-null and non-empty");
        }
        pricingDataProcessingService.processAndSaveCsvPricingData(csvDataList);

        return createCsvDataPostResponse(true, HttpStatus.CREATED, "CSV data received and saved successfully");
    }

    @Override
    public ResponseEntity<ProductPriceDetailGetResponse> getProductPriceDetails(Long productId,
                                                                                Long supplierId) {

        if (productId == null || supplierId == null) {
            throw new IllegalArgumentException("Both productId and supplierId must be provided and non-null");
        }

        ProductPriceDetailEntity priceDetailEntity =
                pricingDataProcessingService.fetchProductPriceDetails(productId, supplierId);

        if (Objects.isNull(priceDetailEntity)) {
            String errorMsg =
                    String.format("No record found for productId: %d and supplierId: %d", productId, supplierId);
            throw new NoRecordFoundException(errorMsg);
        }
        return createProductPriceDetailGetResponse(true, HttpStatus.FOUND, "Record found", priceDetailEntity);
    }

}
