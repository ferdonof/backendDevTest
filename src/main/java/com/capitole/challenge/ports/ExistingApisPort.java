package com.capitole.challenge.ports;

import com.capitole.challenge.business.domain.ProductDetailBO;

public interface ExistingApisPort {
    String[] getProductSimilarIds(String id);
    ProductDetailBO getProductById(String id);
}
