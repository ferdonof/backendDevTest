package com.capitole.challenge.api.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDetail {
    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;

}
