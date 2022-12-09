package com.capitole.challenge.business.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private String id;

    public ProductNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Product not found for id: " + id;
    }
}
