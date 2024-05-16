package com.radek.eshop.service.impl;

import com.radek.eshop.repository.ProductRepository;
import com.radek.eshop.service.ProductService;
import com.radek.eshop.service.dto.ProductDto;
import com.radek.eshop.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<ProductDto> createProduct(ProductDto productDto) {
        return productRepository.save(productMapper.toEntity(productDto))
                .map(productMapper::toDto);
    }

    @Override
    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .map(productMapper::toDto);
    }

    @Override
    public Mono<ProductDto> getProduct(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDto);
    }

    @Override
    public Mono<ProductDto> updateProduct(Long productId, ProductDto productDto) {
        return productRepository.save(productMapper.toEntity(productDto))
                .map(productMapper::toDto);
    }

    @Override
    public Mono<ProductDto> partialUpdateProduct(Long productId, ProductDto productDto) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    productMapper.partialUpdate(product, productDto);
                    return productRepository.save(product)
                            .map(productMapper::toDto);
                });
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        return productRepository.deleteById(productId);
    }

    @Override
    public Mono<Void> deleteProductsNotForSale() {
        return productRepository.deleteProductsNotForSale();
    }
}
