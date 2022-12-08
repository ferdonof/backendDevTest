package com.capitole.challenge.business.usecases.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import com.capitole.challenge.ports.ExistingApisPort;
import org.apache.commons.collections.collection.SynchronizedCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GetSimilarProductsUseCaseImpl implements GetSimilarProductsUseCase {

    private final ExistingApisPort port;

    public GetSimilarProductsUseCaseImpl(ExistingApisPort port) {
        this.port = port;
    }

    @Override
    public SimilarProductsBO execute(String productId) {
        AtomicInteger steps = new AtomicInteger();
        SimilarProductsBO similarProductsBO = new SimilarProductsBO();
        SynchronizedCollection.decorate(similarProductsBO.getSimilarProducts());
        String[] productSimilarIds = port.getProductSimilarIds(productId);

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            CompletionService<ProductDetailBO> executorCompletionService = new ExecutorCompletionService(executorService);
            Arrays.stream(productSimilarIds).forEach( id -> executorCompletionService.submit(new Task(id)));

            while (steps.get() < productSimilarIds.length ){
                steps.incrementAndGet();
                Optional.ofNullable(executorCompletionService.take().get()).ifPresent(detailBo -> similarProductsBO.getSimilarProducts().add(detailBo));
            }

            executorService.shutdownNow();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return similarProductsBO;
    }

    private class Task implements Callable<ProductDetailBO> {
        private final String id;
        public Task(String id){
            this.id = id;
        }

        @Override
        public ProductDetailBO call() {
            try {
                return port.getProductById(id);
            } catch (Exception ex){

            }
            return null;
        }
    }
}
