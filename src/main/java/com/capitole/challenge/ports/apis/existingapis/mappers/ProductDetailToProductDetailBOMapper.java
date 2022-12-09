package com.capitole.challenge.ports.apis.existingapis.mappers;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDetailToProductDetailBOMapper {

    ProductDetailBO map(ProductDetail detail);
}
