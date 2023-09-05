package com.meesho.dmp.common.services;

import com.meesho.dmp.common.dto.CsvData;
import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PricingDataService {

    @Autowired
    private TransactionHandlerService transactionHandlerService;

    private List<ProductPriceDetailEntity> convertDtoToEntities(List<CsvData> csvDataList) {

        return csvDataList.stream()
                          .map(csvData -> ProductPriceDetailEntity
                                  .builder()
                                  .refId(csvData.getId())
                                  .productId(csvData.getProductId())
                                  .supplierId(csvData.getSupplierId())
                                  .wdrpRecommendedPrice(csvData.getWdrpRecommendedPrice())
                                  .recommendedPrice(csvData.getRecommendedPrice())
                                  .build()
                              )
                          .collect(Collectors.toList());
    }

    public void processAndSaveCsvPricingData(List<CsvData> csvDataList) {

        List<ProductPriceDetailEntity> entities = convertDtoToEntities(csvDataList);
        transactionHandlerService.updateAndSaveAllPricingData(entities);
    }

    public ProductPriceDetailEntity fetchProductPriceDetails(Long productId, Long supplierId) {
        return transactionHandlerService.fetchProductPriceDetailsByIds(productId, supplierId);
    }
}
