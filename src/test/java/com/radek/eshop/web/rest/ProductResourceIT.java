package com.radek.eshop.web.rest;

import com.radek.eshop.domain.Product;
import com.radek.eshop.repository.ProductRepository;
import com.radek.eshop.service.dto.ProductDto;
import com.radek.eshop.service.mapper.ProductMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static com.radek.eshop.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductResourceIT {

    private static final Long DEFAULT_PART_NO = 1L;
    private static final Long UPDATED_PART_NO = 2L;

    private static final String DEFAULT_NAME = "Default name";
    private static final String UPDATED_NAME = "Updated name";

    private static final String DEFAULT_DESCRIPTION = "Default description";
    private static final String UPDATED_DESCRIPTION = "Updated description";

    private static final Boolean DEFAULT_IS_FOR_SALE = false;
    private static final Boolean UPDATED_IS_FOR_SALE = true;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    WebTestClient webTestClient;

    private Product product;

    private Product insertedProduct;

    public static Product createEntity() {
        return new Product()
                .partNo(DEFAULT_PART_NO)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .forSale(DEFAULT_IS_FOR_SALE)
                .price(DEFAULT_PRICE);
    }

    @BeforeEach
    public void initTest() {
        product = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct).block();
            insertedProduct = null;
        }
    }

    @Test
    void createProduct() {
        long databaseSizeBeforeCreate = countProducts();

        Product returnedProduct = productMapper.toEntity(
                webTestClient
                        .post()
                        .uri(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productMapper.toDto(product))
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ProductDto.class)
                        .returnResult()
                        .getResponseBody()
        );

        assertThat(countProducts()).isEqualTo(databaseSizeBeforeCreate + 1);
        assertProductsEqual(getSavedProduct(returnedProduct), returnedProduct);

        insertedProduct = returnedProduct;
    }

    @Test
    void getAllProducts() {
        insertedProduct = saveProduct(product);

        webTestClient
                .get()
                .uri(ENTITY_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[*].productId")
                .value(hasItem(insertedProduct.getProductId().intValue()))
                .jsonPath("$.[*].partNo")
                .value(hasItem(DEFAULT_PART_NO.intValue()))
                .jsonPath("$.[*].name")
                .value(hasItem(DEFAULT_NAME))
                .jsonPath("$.[*].description")
                .value(hasItem(DEFAULT_DESCRIPTION))
                .jsonPath("$.[*].forSale")
                .value(hasItem(DEFAULT_IS_FOR_SALE))
                .jsonPath("$.[*].price")
                .value(hasItem(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getProduct() {
        insertedProduct = saveProduct(product);

        webTestClient
                .get()
                .uri(ENTITY_API_URL_ID, product.getProductId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId")
                .value(is(product.getProductId().intValue()))
                .jsonPath("$.partNo")
                .value(is(DEFAULT_PART_NO.intValue()))
                .jsonPath("$.name")
                .value(is(DEFAULT_NAME))
                .jsonPath("$.description")
                .value(is(DEFAULT_DESCRIPTION))
                .jsonPath("$.forSale")
                .value(is(DEFAULT_IS_FOR_SALE))
                .jsonPath("$.price")
                .value(is(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void updateProduct() {
        insertedProduct = saveProduct(product);

        long databaseSizeBeforeUpdate = countProducts();

        Product updatedProduct = getSavedProduct(product);
        updatedProduct
                .partNo(UPDATED_PART_NO)
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .forSale(UPDATED_IS_FOR_SALE)
                .price(UPDATED_PRICE);

        webTestClient
                .put()
                .uri(ENTITY_API_URL_ID, product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productMapper.toDto(updatedProduct))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(countProducts()).isEqualTo(databaseSizeBeforeUpdate);

        assertProductsEqual(getSavedProduct(updatedProduct), updatedProduct);
    }

    @Test
    void partialUpdateProduct() {
        insertedProduct = saveProduct(product);

        long databaseSizeBeforeUpdate = countProducts();

        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setProductId(insertedProduct.getProductId());

        partialUpdatedProduct.partNo(UPDATED_PART_NO).description(UPDATED_DESCRIPTION);

        webTestClient
                .patch()
                .uri(ENTITY_API_URL_ID, product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productMapper.toDto(partialUpdatedProduct))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(countProducts()).isEqualTo(databaseSizeBeforeUpdate);

        Product savedProduct = getSavedProduct(partialUpdatedProduct);
        assertThat(savedProduct.getProductId()).as("check productId").isEqualTo(partialUpdatedProduct.getProductId());
        assertThat(savedProduct.getPartNo()).as("check partNo").isEqualTo(UPDATED_PART_NO);
        assertThat(savedProduct.getName()).as("check name").isEqualTo(DEFAULT_NAME);
        assertThat(savedProduct.getDescription()).as("check description").isEqualTo(UPDATED_DESCRIPTION);
        assertThat(savedProduct.getForSale()).as("check isForSale").isEqualTo(DEFAULT_IS_FOR_SALE);
        assertThat(savedProduct.getPrice()).as("check price").usingComparator(BigDecimal::compareTo).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    void deleteProduct() {
        insertedProduct = saveProduct(product);

        long databaseSizeBeforeDelete = countProducts();

        webTestClient
                .delete()
                .uri(ENTITY_API_URL_ID, product.getProductId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(countProducts()).isEqualTo(databaseSizeBeforeDelete - 1);
    }

    @Test
    void deleteProductsNotForSale() {
        saveProduct(product);
        product.setProductId(null);
        saveProduct(product);

        long databaseSizeBeforeDelete = countProducts();
        long countProductsNotForSale = productRepository.countProductsNotForSale().block();

        webTestClient
                .delete()
                .uri(ENTITY_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(countProducts()).isEqualTo(databaseSizeBeforeDelete - countProductsNotForSale);
    }

    private long countProducts() {
        return productRepository.count().block();
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product).block();
    }

    private Product getSavedProduct(Product product) {
        return productRepository.findById(product.getProductId()).block();
    }

    private void assertProductsEqual(Product actual, Product expected) {
        assertThat(actual)
                .as("Verify Product relevant properties")
                .satisfies(e -> assertThat(e.getProductId()).as("check productId").isEqualTo(expected.getProductId()))
                .satisfies(e -> assertThat(e.getPartNo()).as("check partNo").isEqualTo(expected.getPartNo()))
                .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(expected.getName()))
                .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(expected.getDescription()))
                .satisfies(e -> assertThat(e.getForSale()).as("check isForSale").isEqualTo(expected.getForSale()))
                .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(BigDecimal::compareTo).isEqualTo(expected.getPrice()));
    }
}
