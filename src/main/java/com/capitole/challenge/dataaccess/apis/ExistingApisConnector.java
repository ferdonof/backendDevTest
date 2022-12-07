package com.capitole.challenge.dataaccess.apis;

import com.capitole.challenge.dataaccess.apis.existingapis.responses.ProductDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "existingApis", url = "http://${existing_apis.host}:${existing_apis.port}/${existing_apis.endpoint}")
public interface ExistingApisConnector {

    @GetMapping("/{productId}/similarids")
    public String[] getProductSimilarIds(@PathVariable(name = "productId") String productId);

    @GetMapping("/{productId}")
    public ProductDetail getProduct(@PathVariable(name = "productId") String productId);
}
