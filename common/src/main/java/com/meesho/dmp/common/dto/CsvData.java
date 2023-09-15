package com.meesho.dmp.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "product_id", "supplier_id", "recommended_price", "wdrp_recommended_price"}) // TODO: add
// comment

public class CsvData {
    @NotNull
    private Long id;

    @JsonProperty("product_id")
    @NotNull
    private Long productId;

    @JsonProperty("supplier_id")
    @NotNull
    private Long supplierId;

    @JsonProperty("recommended_price")
    @NotNull
    private double recommendedPrice;

    @JsonProperty("wdrp_recommended_price")
    @NotNull
    private double wdrpRecommendedPrice;

}
