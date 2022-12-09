package com.capitole.challenge.ports.apis.existingapis.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.dataaccess.apis.ExistingApisConnector;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.ExistingApisPort;
import feign.FeignException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExistingApisPortImpl implements ExistingApisPort {

    private final ExistingApisConnector connector;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ExistingApisPortImpl(ExistingApisConnector connector, CircuitBreakerFactory circuitBreakerFactory) {
        this.connector = connector;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public String[] getProductSimilarIds(String id) {
        try {
            return connector.getProductSimilarIds(id);
        } catch (FeignException ex) {
            if (FeignException.NotFound.class.isAssignableFrom(ex.getClass()))
                throw new SimilarProductNotFoundException(id);

            throw new ExternalApiException(id, ex);
        }

    }

    @Override
    public ProductDetailBO getProductById(String id) {
        ProductDetail product = circuitBreakerFactory.create("getProduct").run(
                () -> connector.getProduct(id),
                pd -> new ProductDetail(id, "Can't get product info. Please try later.", BigDecimal.ZERO, false)
        );

        ProductDetailBO bo = new ProductDetailBO(product.getId(), product.getName(), product.getPrice(), product.getAvailability());
        return bo;
    }
}
