package com.capitole.challenge.unit.mappers;

import com.capitole.challenge.api.dtos.responses.ProductDetail;
import com.capitole.challenge.api.dtos.responses.SimilarProducts;
import com.capitole.challenge.api.mappers.SimilarProductsBoToSimilarProductsMapper;
import com.capitole.challenge.api.mappers.SimilarProductsBoToSimilarProductsMapperImpl;
import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

class SimilarProductsBoToSimilarProductsMapperTest {
    private SimilarProductsBoToSimilarProductsMapper mapper = new SimilarProductsBoToSimilarProductsMapperImpl();

    @Test
    void givenNull_whenMap_thenReturnNull() {
        Assertions.assertThat(mapper.map(null)).isNull();
    }

    @Test
    void givenProductDetail_whenMap_thenReturnProductDetailBO() {
        SimilarProductsBO similar = new SimilarProductsBO();
        similar.addProduct(new ProductDetailBO("1", "Water power robot", BigDecimal.TEN, true));
        SimilarProducts expected = new SimilarProducts();
        expected.setSimilarProducts(Arrays.asList(new ProductDetail("1", "Water power robot", BigDecimal.TEN, true)));
        Assertions
                .assertThat(mapper.map(similar))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
