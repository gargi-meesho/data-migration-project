package com.meesho.dmp.common.utils;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.models.response.ErrorResponse;
import com.meesho.dmp.common.models.response.ProductPriceDetailGetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

public class CommonUtils {

    public static ResponseEntity<CsvDataPostResponse> createCsvDataPostResponse(boolean success, HttpStatus status,
                                                                                String message) {
        CsvDataPostResponse response = CsvDataPostResponse.builder()
                                                          .success(success)
                                                          .message(message)
                                                          .status(status)
                                                          .created_at(ZonedDateTime.now())
                                                          .build();
        return ResponseEntity.status(response.getStatus())
                             .body(response);
    }

    public static ResponseEntity<ProductPriceDetailGetResponse> createProductPriceDetailGetResponse(boolean success,
                                                                                                    HttpStatus status,
                                                                                                    String message,
                                                                                                    ProductPriceDetailEntity data) {
        ProductPriceDetailGetResponse response = ProductPriceDetailGetResponse.builder()
                                                                              .success(success)
                                                                              .status(status)
                                                                              .message(message)
                                                                              .data(data)
                                                                              .build();

        return ResponseEntity.status(response.getStatus())
                             .body(response);
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String errors) {
        ErrorResponse response = ErrorResponse.builder()
                                              .status(status)
                                              .errors(errors)
                                              .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
