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
    @NotNull
    private Long refId;

    @Column(name = "product_id")
    @NotNull
    private Long productId;

    @Column(name = "supplier_id")
    @NotNull
    private Long supplierId;

    @Column(name = "recommended_price")
    @Min(value = 50, message = "Recommended price should be at least Rs 50")
    @NotNull
    private double recommendedPrice;

    @Column(name = "wdrp_recommended_price")
    @Min(value = 50, message = "WRDP recommended price should e at least Rs 50")
    @NotNull
    private double wdrpRecommendedPrice;

    @AssertTrue(message = "WRDP recommended price must be less than the recommended price")
    public boolean isValidWrdpRecommendedPrice() {
        return recommendedPrice > wdrpRecommendedPrice;
    }

}
