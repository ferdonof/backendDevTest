package com.capitole.challenge.ports.apis.existingapis.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.ProductNotFoundException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.dataaccess.apis.ExistingApisConnector;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.ExistingApisPort;
import feign.FeignException;
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
            if (FeignException.NotFound.class.isAssignableFrom(ex.getClass()))
                throw new SimilarProductNotFoundException(id);

            throw new ExternalApiException(id, ex);
        }

    }

    @Override
    public ProductDetailBO getProductById(String id) {
        try {
            ProductDetail product = connector.getProduct(id);
            ProductDetailBO bo = new ProductDetailBO(product.getId(), product.getName(), product.getPrice(), product.getAvailability());
            return bo;
        } catch (FeignException ex) {
            if (FeignException.NotFound.class.isAssignableFrom(ex.getClass()))
                throw new ProductNotFoundException(id);

            throw new ExternalApiException(id, ex);
        }
    }
}
