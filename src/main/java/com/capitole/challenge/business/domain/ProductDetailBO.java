package com.capitole.challenge.business.domain;

import java.math.BigDecimal;

public class ProductDetailBO {
    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;

    public ProductDetailBO() {
    }

    public ProductDetailBO(String id, String name, BigDecimal price, boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
