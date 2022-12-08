package com.capitole.challenge.ports.apis.existingapis.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.dataaccess.apis.ExistingApisConnector;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.ExistingApisPort;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExistingApisPortImpl implements ExistingApisPort {

    private final ExistingApisConnector connector;

    public ExistingApisPortImpl(ExistingApisConnector connector) {
        this.connector = connector;
    }

    @Override
    public String[] getProductSimilarIds(String id) {
        try {
            return connector.getProductSimilarIds(id);
        } catch (FeignException ex) {
            if (FeignException.FeignClientException.class.isAssignableFrom(ex.getClass()) &&
                    ex.status() == HttpStatus.NOT_FOUND.value())
                throw new SimilarProductNotFoundException(id);

            throw new ExternalApiException(id, ex);
        }

    }

    @Override
    public ProductDetailBO getProductById(String id) {
        ProductDetail product = connector.getProduct(id);
        ProductDetailBO bo = new ProductDetailBO(product.getId(), product.getName(), product.getPrice(), product.getAvailability());
        return bo;
    }
}
