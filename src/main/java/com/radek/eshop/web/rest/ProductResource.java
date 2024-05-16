package com.radek.eshop.web.rest;

import com.radek.eshop.service.ProductService;
import com.radek.eshop.service.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("api/products")
@CrossOrigin
@Transactional
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public Mono<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        log.info("REST request to save Product : {}", productDto);
        return productService.createProduct(productDto);
    }

    @GetMapping("")
    public Flux<ProductDto> getAllProducts() {
        log.info("REST request to get all Products");
        return productService.getAllProducts();
    }

    @GetMapping("{productId}")
    public Mono<ProductDto> getProduct(@PathVariable("productId") Long productId) {
        log.info("REST request to get Product : {}", productId);
        return productService.getProduct(productId);
    }

    @PutMapping("{productId}")
    public Mono<ProductDto> updateProduct(
            @PathVariable(value = "productId", required = false) final Long productId,
            @RequestBody ProductDto productDto) {
        log.info("REST request to update Product : {}, {}", productId, productDto);
        return productService.updateProduct(productId, productDto);
    }

    @PatchMapping("{productId}")
    public Mono<ProductDto> partialUpdateProduct(
            @PathVariable(value = "productId", required = false) final Long productId,
            @RequestBody ProductDto productDto) {
        log.info("REST request to partial update Product partially : {}, {}", productId, productDto);
        return productService.partialUpdateProduct(productId, productDto);
    }

    @DeleteMapping("{productId}")
    public Mono<Void> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("REST request to delete Product : {}", productId);
        return productService.deleteProduct(productId);
    }

    @DeleteMapping("")
    public Mono<Void> deleteProductsNotForSale() {
        log.info("REST request to delete not for sale Products");
        return productService.deleteProductsNotForSale();
    }
}
