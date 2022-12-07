package com.capitole.challenge.configurations;

import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import com.capitole.challenge.business.usecases.impl.GetSimilarProductsUseCaseImpl;
import com.capitole.challenge.ports.ExistingApisPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseBeanConfiguration {

    @Bean
    public GetSimilarProductsUseCase getSimilarProductsUseCase(ExistingApisPort port){
        return new GetSimilarProductsUseCaseImpl(port);
    }
}
