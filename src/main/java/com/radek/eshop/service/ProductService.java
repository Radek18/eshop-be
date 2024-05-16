package com.radek.eshop.service;

import com.radek.eshop.service.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<ProductDto> createProduct(ProductDto productDto);

    Flux<ProductDto> getAllProducts();

    Mono<ProductDto> getProduct(Long productId);

    Mono<ProductDto> updateProduct(Long productId, ProductDto productDto);

    Mono<ProductDto> partialUpdateProduct(Long productId, ProductDto productDto);

    Mono<Void> deleteProduct(Long productId);

    Mono<Void> deleteProductsNotForSale();
}
