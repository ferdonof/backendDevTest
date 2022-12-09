package com.capitole.challenge.unit.ports;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.ProductNotFoundException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.dataaccess.apis.ExistingApisConnector;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import com.capitole.challenge.ports.ExistingApisPort;
import com.capitole.challenge.ports.apis.existingapis.impl.ExistingApisPortImpl;
import com.capitole.challenge.ports.apis.existingapis.mappers.ProductDetailToProductDetailBOMapper;
import feign.FeignException;
import feign.Request;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ExistingApisPortTest {

    private ExistingApisConnector connector = mock(ExistingApisConnector.class);
    private CircuitBreakerFactory circuitBreakerFactory = mock(CircuitBreakerFactory.class);

    private ProductDetailToProductDetailBOMapper mapper = mock(ProductDetailToProductDetailBOMapper.class);

    private ExistingApisPort port = new ExistingApisPortImpl(connector, circuitBreakerFactory, mapper);

    @Test
    void givenProductIdExists_whenGetProductSimilarIds_thenReturnStringArrayWithIds() {
        when(connector.getProductSimilarIds(anyString())).thenReturn(new String[]{"1", "2", "3"});

        String[] result = port.getProductSimilarIds("1");

        verify(connector, times(1)).getProductSimilarIds("1");

        Assertions
                .assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new String[]{"1", "2", "3"});

    }


    @Test
    void givenProductIdNotExists_whenGetProductSimilarIds_thenThrowSimilarProductNotFoundException() {
        when(connector.getProductSimilarIds(anyString())).thenThrow(getNotFound());

        Assertions
                .assertThatThrownBy(() -> port.getProductSimilarIds("1"))
                .isExactlyInstanceOf(SimilarProductNotFoundException.class)
                .hasMessage("Similar product ids not found for Product id: 1");

        verify(connector, times(1)).getProductSimilarIds("1");

    }

    @Test
    void givenProductId_whenGetProductSimilarIdsAndExternalServiceFail_thenThrowExternalApiException() {
        when(connector.getProductSimilarIds(anyString())).thenThrow(getInternalServerError());

        Assertions
                .assertThatThrownBy(() -> port.getProductSimilarIds("1"))
                .isExactlyInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Error getting product with id: 1.");

        verify(connector, times(1)).getProductSimilarIds("1");

    }

    private static FeignException.InternalServerError getInternalServerError() {
        return new FeignException.InternalServerError(null,
                Request.create(Request.HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
                null, Collections.emptyMap());
    }

    @Test
    void givenProductId_whenGetProduct_thenReturnProductDetailBO() {
        ProductDetail detail = new ProductDetail("1", "Water power robot", BigDecimal.TEN, true);
        when(connector.getProduct(anyString())).thenReturn(detail);
        when(mapper.map(any())).thenReturn(new ProductDetailBO("1", "Water power robot", BigDecimal.TEN, true));

        ProductDetailBO result = port.getProductById("1");

        verify(connector, times(1)).getProduct("1");
        verify(mapper, times(1)).map(detail);

        Assertions
                .assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new ProductDetailBO("1", "Water power robot", BigDecimal.TEN, true));

    }

    @Test
    void givenProductId_whenGetProductWithNonExistentProduct_thenThrowProductNotFoundException() {
        when(connector.getProduct(anyString())).thenThrow(getNotFound());

        Assertions.assertThatThrownBy(() -> port.getProductById("1"))
                .isExactlyInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found for id: 1");

        verify(connector, times(1)).getProduct("1");

    }

    private static FeignException.NotFound getNotFound() {
        return new FeignException.NotFound(null,
                Request.create(Request.HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
                null, Collections.emptyMap());
    }

    @Test
    void givenProductId_whenGetProductAndExternalServiceFail_thenThrowExternalApiException() {
        when(connector.getProduct(anyString())).thenThrow(getInternalServerError());

        Assertions.assertThatThrownBy(() -> port.getProductById("1"))
                .isExactlyInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Error getting product with id: 1. ");

        verify(connector, times(1)).getProduct("1");

    }
}
