package com.meesho.dmp.web.controller;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.exceptions.NoRecordFoundException;
import com.meesho.dmp.common.models.ApiResponse;
import com.meesho.dmp.common.models.CsvDataPostRequest;
import com.meesho.dmp.web.helpers.CsvDataMigrationHelper;
import com.meesho.dmp.web.services.CsvDataMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.meesho.dmp.common.utils.CommonUtils.createApiResponse;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CsvDataMigrationController {

    private static final String LOG_PREFIX = "[CsvDataMigrationController]";
    @Autowired
    private CsvDataMigrationService csvDataMigrationService;
    @Autowired
    private CsvDataMigrationHelper csvDataMigrationHelper;

    @PostMapping(value = "/save/pricing-data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> postCsvData(@Valid @RequestBody CsvDataPostRequest request) {


        List<CsvData> csvDataItems = request.getCsvDataList();
        log.info("{} postCsvData request: {}", LOG_PREFIX, csvDataItems);

        csvDataMigrationService.processAndSaveAllData(csvDataItems);

        return createApiResponse(true, HttpStatus.CREATED, "CSV data received and saved successfully");

    }

    @GetMapping("/pricing-data")
    public ResponseEntity<ApiResponse<ProductPriceDetailEntity>> getPriceDetails(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "supplierId") Long supplierId) {

        log.info("{} getPriceDetails productId: {}, supplierId: {}", LOG_PREFIX, productId, supplierId);

        ProductPriceDetailEntity productPriceDetails =
                csvDataMigrationService.getProductPriceDetails(productId, supplierId);

        if (Objects.isNull(productPriceDetails)) {
            String errorMsg =
                    String.format("No record found for productId: %d and supplierId: %d", productId, supplierId);
            throw new NoRecordFoundException(errorMsg);
        }
        return createApiResponse(true, HttpStatus.FOUND, "Record found", productPriceDetails);

    }
}
