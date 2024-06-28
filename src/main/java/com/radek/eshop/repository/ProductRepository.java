package com.radek.eshop.repository;

import com.radek.eshop.domain.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("DELETE FROM product WHERE is_for_sale = false")
    Mono<Void> deleteProductsNotForSale();

    @Query("SELECT count(*) FROM product WHERE is_for_sale = false")
    Mono<Long> countProductsNotForSale();
}
