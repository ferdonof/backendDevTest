package com.capitole.challenge.functional;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SimilarProductsControllerTest extends ControllerTest {


    @Autowired
    TestRestTemplate rest;

    @BeforeEach
    void setup() {
        WireMock.resetAllRequests();
    }

    @Test
    void doGet_withProductId1_thenResponds200Ok() {

        ResponseEntity<SimilarProductsBO> response = rest.getForEntity("/product/1/similar", SimilarProductsBO.class);


        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/1/similarids")));
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/2")));
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/3")));
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/4")));

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        SimilarProductsBO obtained = response.getBody();

        SimilarProductsBO similarProductsBO = new SimilarProductsBO();
        similarProductsBO
                .addProduct(new ProductDetailBO("2", "Dress", new BigDecimal("19.99"), true));
        similarProductsBO
                .addProduct(new ProductDetailBO("3", "Blazer", new BigDecimal("29.99"), false));
        similarProductsBO
                .addProduct(new ProductDetailBO("4", "Boots", new BigDecimal("39.99"), true));

        Assertions.assertThat(obtained).usingRecursiveComparison().isEqualTo(similarProductsBO);
    }

    private Object getExpected() {
        return new ArrayList<>();
    }


}
