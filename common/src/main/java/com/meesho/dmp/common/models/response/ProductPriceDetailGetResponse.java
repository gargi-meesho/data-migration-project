package com.meesho.dmp.common.models.response;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceDetailGetResponse {
    private boolean success;
    private HttpStatus status;
    private String message;
    private ProductPriceDetailEntity data;
}
