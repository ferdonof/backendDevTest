package com.capitole.challenge.unit.mappers;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.apis.existingapis.mappers.ProductDetailToProductDetailBOMapper;
import com.capitole.challenge.ports.apis.existingapis.mappers.ProductDetailToProductDetailBOMapperImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductDetailToProductDetailBOMapperTest {
    private ProductDetailToProductDetailBOMapper mapper = new ProductDetailToProductDetailBOMapperImpl();

    @Test
    void givenNull_whenMap_thenReturnNull() {
        Assertions.assertThat(mapper.map(null)).isNull();
    }

    @Test
    void givenProductDetail_whenMap_thenReturnProductDetailBO() {
        Assertions
                .assertThat(mapper.map(new ProductDetail("1", "Water power robot", BigDecimal.TEN, true)))
                .usingRecursiveComparison()
                .isEqualTo(new ProductDetailBO("1", "Water power robot", BigDecimal.TEN, true));
    }
}
