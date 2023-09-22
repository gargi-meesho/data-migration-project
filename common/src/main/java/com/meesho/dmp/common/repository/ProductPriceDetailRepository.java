package com.meesho.dmp.common.repository;

import com.meesho.dmp.common.entities.ProductPriceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceDetailRepository extends JpaRepository<ProductPriceDetailEntity, Long> {

    ProductPriceDetailEntity findByProductIdAndSupplierId(Long productId, Long supplierId);
}
