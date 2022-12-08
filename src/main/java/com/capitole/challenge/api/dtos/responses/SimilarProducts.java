package com.capitole.challenge.api.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SimilarProducts {
    private List<ProductDetail> SimilarProducts;
}
