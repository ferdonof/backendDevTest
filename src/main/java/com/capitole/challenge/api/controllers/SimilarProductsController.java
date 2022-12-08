package com.capitole.challenge.api.controllers;

import com.capitole.challenge.api.dtos.responses.SimilarProducts;
import com.capitole.challenge.api.mappers.SimilarProductsBoToSimilarProductsMapper;
import com.capitole.challenge.business.exceptions.ExternalApiException;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class SimilarProductsController {

    private final GetSimilarProductsUseCase useCase;
    private final SimilarProductsBoToSimilarProductsMapper mapper;

    public SimilarProductsController(GetSimilarProductsUseCase useCase, SimilarProductsBoToSimilarProductsMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @GetMapping("/{productId}/similar")
    @ResponseStatus(HttpStatus.OK)
    public SimilarProducts getProductSimilar(@PathVariable(name = "productId") String productId) {
        return mapper.map(useCase.execute(productId));
    }

    @ExceptionHandler(ExternalApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String externalApiException(ExternalApiException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SimilarProductNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(SimilarProductNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
