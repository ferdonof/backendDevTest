package com.capitole.challenge.unit.usecases;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import com.capitole.challenge.business.usecases.impl.GetSimilarProductsUseCaseImpl;
import com.capitole.challenge.ports.ExistingApisPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GetSimilarProductsUseCaseTest {

    private ExistingApisPort port = mock(ExistingApisPort.class);
    private GetSimilarProductsUseCase useCase = new GetSimilarProductsUseCaseImpl(port);

    @Test
    void givenValidProductId_whenExecuteHavingThreeSimilarProducts_thenReturnSimilarProductBoWithThreeItems() {

        when(port.getProductSimilarIds(anyString())).thenReturn(new String[]{"1", "2", "3"});
        ProductDetailBO dress = new ProductDetailBO("2", "Dress", new BigDecimal("19.99"), true);
        ProductDetailBO blazer = new ProductDetailBO("3", "Blazer", new BigDecimal("29.99"), false);
        ProductDetailBO boots = new ProductDetailBO("4", "Boots", new BigDecimal("39.99"), true);

        when(port.getProductById(anyString()))
                .thenReturn(dress)
                .thenReturn(blazer)
                .thenReturn(boots);

        SimilarProductsBO result = useCase.execute("1");

        verify(port, times(1)).getProductSimilarIds("1");
        verify(port, times(3)).getProductById(anyString());

        SimilarProductsBO expected = new SimilarProductsBO();
        expected.addProduct(dress);
        expected.addProduct(blazer);
        expected.addProduct(boots);

        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);

    }

}
