package com.meesho.dmp.web.controller;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.models.request.CsvDataPostRequest;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.models.response.ProductPriceDetailGetResponse;
import com.meesho.dmp.web.services.CsvDataMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CsvDataMigrationController {

    private static final String LOG_PREFIX = "[CsvDataMigrationController]";
    @Autowired
    private CsvDataMigrationService csvDataMigrationService;

    @PostMapping(value = "/save/pricing-data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CsvDataPostResponse> postCsvData(@Valid @RequestBody CsvDataPostRequest request) {

        List<CsvData> csvDataItems = request.getCsvDataList();
        log.info("{} postCsvData request: {}", LOG_PREFIX, csvDataItems);

        return csvDataMigrationService.processAndSaveAllData(csvDataItems);
    }

    @GetMapping("/fetch/pricing-data")
    public ResponseEntity<ProductPriceDetailGetResponse> getPriceDetails(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "supplierId") Long supplierId) {

        log.info("{} getPriceDetails productId: {}, supplierId: {}", LOG_PREFIX, productId, supplierId);

        return csvDataMigrationService.getProductPriceDetails(productId, supplierId);
    }
}
