package com.capitole.challenge.business.domain;

import java.util.ArrayList;
import java.util.List;

public class SimilarProductsBO {
    private List<ProductDetailBO> similarProducts = new ArrayList<>();


    public List<ProductDetailBO> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<ProductDetailBO> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public void addProduct(ProductDetailBO detail) {
        this.similarProducts.add(detail);
    }

    @Override
    public String toString() {
        return "SimilarProductsBO{" +
                "similarProducts=" + similarProducts +
                '}';
    }
}
