package com.capitole.challenge.business.usecases.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import com.capitole.challenge.ports.ExistingApisPort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetSimilarProductsUseCaseImpl implements GetSimilarProductsUseCase {

    private final ExistingApisPort port;

    public GetSimilarProductsUseCaseImpl(ExistingApisPort port) {
        this.port = port;
    }

    @Override
    public SimilarProductsBO execute(String productId) {
        String[] productSimilarIds = port.getProductSimilarIds(productId);
        List<ProductDetailBO> productsDetails = Arrays.asList(productSimilarIds).stream().map(id -> port.getProductById(id)).collect(Collectors.toList());
        SimilarProductsBO similarProductsBO = new SimilarProductsBO();
        similarProductsBO.setSimilarProducts(productsDetails);
        return similarProductsBO;
    }
}
