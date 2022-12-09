package com.capitole.challenge.business.exceptions;

public class SimilarProductNotFoundException extends RuntimeException {
    private String id;

    public SimilarProductNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Similar product ids not found for Product id: " + id;
    }
}
