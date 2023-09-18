package com.meesho.dmp.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
// JsonPropertyOrder required for one-to-one mapping with CSV file header
@JsonPropertyOrder({"id", "product_id", "supplier_id", "recommended_price", "wdrp_recommended_price"})

public class CsvData {
    @NotNull(message = "id cannot be null")
    private Long id;

    @JsonProperty("product_id")
    @NotNull(message = "productId cannot be null")
    private Long productId;

    @JsonProperty("supplier_id")
    @NotNull(message = "supplierId cannot be null")
    private Long supplierId;

    @JsonProperty("recommended_price")
    @NotNull(message = "recommendedPrice cannot be null")
    private Double recommendedPrice;

    @JsonProperty("wdrp_recommended_price")
    @NotNull(message = "wrdpRecommendedPrice cannot be null")
    private Double wdrpRecommendedPrice;

}
