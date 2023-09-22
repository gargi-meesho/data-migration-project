package com.meesho.dmp.common.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "csv_pricing_data")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceDetailEntity extends BaseEntity {

    @Column(name = "ref_id")
    @NotNull(message = "id cannot be null")
    private Long refId;

    @Column(name = "product_id")
    @NotNull(message = "productId cannot be null")
    private Long productId;

    @Column(name = "supplier_id")
    @NotNull(message = "supplierId cannot be null")
    private Long supplierId;

    @Column(name = "recommended_price")
    @Min(value = 1, message = "Recommended price should be at least Rs 1")
    @NotNull(message = "recommendedPrice cannot be null")
    private Double recommendedPrice;

    @Column(name = "wdrp_recommended_price")
    @Min(value = 1, message = "WRDP recommended price should e at least Rs 1")
    @NotNull(message = "wdrpRecommendedPrice cannot be null")
    private Double wdrpRecommendedPrice;

    @AssertTrue(message = "WRDP recommended price must be less than the recommended price")
    public boolean isValidWrdpRecommendedPrice() {
        return recommendedPrice > wdrpRecommendedPrice;
    }

}
