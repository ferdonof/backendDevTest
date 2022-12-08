package com.capitole.challenge.business.exceptions;

public class ExternalApiException extends RuntimeException {
    private String id;
    private Exception ex;

    public ExternalApiException(String id, Exception ex) {
        this.id = id;
        this.ex = ex;
    }

    @Override
    public String getMessage() {
        return "Error getting product with id: " + id + ". " + ex.getMessage();
    }

    public Exception getEx() {
        return ex;
    }
}