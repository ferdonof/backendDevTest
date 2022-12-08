package com.capitole.challenge.functional;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SimilarProductsControllerTest extends ControllerTest {


    @Autowired
    TestRestTemplate rest;

    @BeforeEach
    void setup() {
        WireMock.resetAllRequests();
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    void doGet_withProductId1_thenResponds200Ok(String id, List<String> productIds, List<ProductDetailBO> details, HttpStatus status ) {

        ResponseEntity<SimilarProductsBO> response = rest.getForEntity("/product/"+id+"/similar", SimilarProductsBO.class);


        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/"+id+"/similarids")));

        productIds.forEach(pid -> {
            WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/product/"+pid)));
        });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(status);
        SimilarProductsBO obtained = response.getBody();

        SimilarProductsBO similarProductsBO = new SimilarProductsBO();
        similarProductsBO.getSimilarProducts().addAll(details);

        Assertions
                .assertThat(obtained)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(similarProductsBO);
    }


    private static Stream<Arguments> getParameters(){
        Map<String, List> detailsMap = Map.of(
                "1", Arrays.asList(
                        new ProductDetailBO("2",    "Dress", new BigDecimal("19.99"), true),
                        new ProductDetailBO("3",    "Blazer", new BigDecimal("29.99"), false),
                        new ProductDetailBO("4",    "Boots", new BigDecimal("39.99"), true)),
                "2", Arrays.asList(
                        new ProductDetailBO("3",    "Blazer", new BigDecimal("29.99"), false),
                        new ProductDetailBO("100",  "Trousers", new BigDecimal("49.99"), false),
                        new ProductDetailBO("1000", "Coat", new BigDecimal("89.99"), true)),
                "4", Arrays.asList(
                        new ProductDetailBO("1", "Shirt", new BigDecimal("9.99"), true),
                        new ProductDetailBO("2", "Dress", new BigDecimal("19.99"), true)),
                "5", Arrays.asList(
                        new ProductDetailBO("1", "Shirt", new BigDecimal("9.99"), true),
                        new ProductDetailBO("2", "Dress", new BigDecimal("19.99"), true))
        );

        return Stream.of(
                Arguments.of("1", Arrays.asList("2","3","4"), detailsMap.get("1"), HttpStatus.OK),
                Arguments.of("2", Arrays.asList("3","100","1000"), detailsMap.get("2"), HttpStatus.OK),
                Arguments.of("4", Arrays.asList("1","2","5"), detailsMap.get("4"), HttpStatus.OK),
                Arguments.of("5", Arrays.asList("1","2","6"), detailsMap.get("5"), HttpStatus.OK)
        );

    }



}
