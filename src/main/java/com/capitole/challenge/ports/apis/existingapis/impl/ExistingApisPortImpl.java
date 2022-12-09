package com.capitole.challenge.ports.apis.existingapis.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.ProductNotFoundException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.dataaccess.apis.ExistingApisConnector;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.ExistingApisPort;
import feign.FeignException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
        return circuitBreakerFactory.create("get-similar-product-ids").run(
                () -> connector.getProductSimilarIds(id),
                throwable -> {
                    if (FeignException.FeignClientException.class.isAssignableFrom(throwable.getCause().getClass())) {
                        throw new SimilarProductNotFoundException(id);
                    }
                    throw new ExternalApiException(id, (Exception) throwable.getCause());
                });

    }

    @Override
    public ProductDetailBO getProductById(String id) {

        ProductDetail product = circuitBreakerFactory.create("get-product").run(
                () -> connector.getProduct(id),
                throwable -> {
                    if (FeignException.FeignClientException.class.isAssignableFrom(throwable.getCause().getClass()) &&
                            ((FeignException.FeignClientException) throwable.getCause()).status() == HttpStatus.NOT_FOUND.value()) {
                        throw new ProductNotFoundException(id);
                    } else throw new ExternalApiException(id, (Exception) throwable.getCause());

                });

        ProductDetailBO bo = new ProductDetailBO(product.getId(), product.getName(), product.getPrice(), product.getAvailability());
        return bo;

    }
}
