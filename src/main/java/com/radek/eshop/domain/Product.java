package com.radek.eshop.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("product")
public class Product {

    @Id
    @Column("product_id")
    private Long productId;

    @Column("part_no")
    private Long partNo;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("is_for_sale")
    private Boolean isForSale;

    @Column("price")
    private BigDecimal price;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPartNo() {
        return partNo;
    }

    public void setPartNo(Long partNo) {
        this.partNo = partNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getForSale() {
        return isForSale;
    }

    public void setForSale(Boolean forSale) {
        isForSale = forSale;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", partNo=" + partNo +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isForSale=" + isForSale +
                ", price=" + price +
                '}';
    }
}
