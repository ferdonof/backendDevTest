package com.capitole.challenge.dataaccess.apis.existingapis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
    private String id;
    private String name;
    private BigDecimal price;
    private Boolean availability;

}
