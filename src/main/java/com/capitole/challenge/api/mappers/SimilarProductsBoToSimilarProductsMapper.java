package com.capitole.challenge.api.mappers;

import com.capitole.challenge.api.dtos.responses.SimilarProducts;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimilarProductsBoToSimilarProductsMapper {

    SimilarProducts map(SimilarProductsBO similarProductsBO);
}
