package com.capitole.challenge.business.usecases;

import com.capitole.challenge.business.domain.SimilarProductsBO;

public interface GetSimilarProductsUseCase {
    SimilarProductsBO execute(String productId);
}
