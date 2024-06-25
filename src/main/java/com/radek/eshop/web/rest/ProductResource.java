package com.radek.eshop.web.rest;

import com.radek.eshop.service.ProductService;
import com.radek.eshop.service.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/products")
@CrossOrigin
@Transactional
public class ProductResource {

    private static final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<ProductDto>> createProduct(@RequestBody ProductDto productDto) {
        log.info("REST request to save Product : {}", productDto);

        return productService.createProduct(productDto)
                .map(result -> {
                    try {
                        return ResponseEntity
                                .created(new URI("api/products/" + result.getProductId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getProductId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping("")
    public Mono<ResponseEntity<Flux<ProductDto>>> getAllProducts() {
        log.info("REST request to get all Products");

        return Mono.just(
                ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createAlert(applicationName, applicationName + ".products.fetched", ""))
                        .body(productService.getAllProducts())
        );
    }

    @GetMapping("{productId}")
    public Mono<ResponseEntity<ProductDto>> getProduct(@PathVariable("productId") Long productId) {
        log.info("REST request to get Product : {}", productId);

        return ResponseUtil.wrapOrNotFound(productService.getProduct(productId));
    }

    @PutMapping("{productId}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(
            @PathVariable(value = "productId", required = false) final Long productId,
            @RequestBody ProductDto productDto) {
        log.info("REST request to update Product : {}, {}", productId, productDto);

        return productService.updateProduct(productId, productDto)
                .map(result -> ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getProductId().toString()))
                        .body(result)
                );
    }

    @PatchMapping("{productId}")
    public Mono<ResponseEntity<ProductDto>> partialUpdateProduct(
            @PathVariable(value = "productId", required = false) final Long productId,
            @RequestBody ProductDto productDto) {
        log.info("REST request to partial update Product partially : {}, {}", productId, productDto);

        return productService.partialUpdateProduct(productId, productDto)
                .map(result -> ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getProductId().toString()))
                        .body(result)
                );
    }

    @DeleteMapping("{productId}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("REST request to delete Product : {}", productId);

        return productService.deleteProduct(productId)
                .then(
                        Mono.just(
                                ResponseEntity
                                        .noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, productId.toString()))
                                        .build()
                ));
    }

    @DeleteMapping("")
    public Mono<ResponseEntity<Void>> deleteProductsNotForSale() {
        log.info("REST request to delete not for sale Products");

        return productService.deleteProductsNotForSale()
                .then(
                        Mono.just(
                                ResponseEntity
                                        .noContent()
                                        .headers(HeaderUtil.createAlert(applicationName, applicationName + ".products.deleted", ""))
                                        .build()
                        )
                );
    }
}
