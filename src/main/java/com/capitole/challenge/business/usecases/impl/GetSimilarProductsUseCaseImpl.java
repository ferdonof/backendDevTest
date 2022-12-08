package com.capitole.challenge.business.usecases.impl;

import com.capitole.challenge.business.domain.ProductDetailBO;
import com.capitole.challenge.business.domain.SimilarProductsBO;
import com.capitole.challenge.business.exceptions.SimilarProductNotFoundException;
import com.capitole.challenge.business.usecases.GetSimilarProductsUseCase;
import com.capitole.challenge.ports.ExistingApisPort;
import org.apache.commons.collections.collection.SynchronizedCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GetSimilarProductsUseCaseImpl implements GetSimilarProductsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetSimilarProductsUseCaseImpl.class);
    private final ExistingApisPort port;

    public GetSimilarProductsUseCaseImpl(ExistingApisPort port) {
        this.port = port;
    }

    @Override
    public SimilarProductsBO execute(String productId) {
        AtomicInteger steps = new AtomicInteger();
        SimilarProductsBO similarProductsBO = new SimilarProductsBO();
        SynchronizedCollection.decorate(similarProductsBO.getSimilarProducts());

        try {
            logger.info("Request similar products of product id: {}", productId);
            String[] productSimilarIds = port.getProductSimilarIds(productId);
            logger.info("Similar ids: {}", productSimilarIds);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            CompletionService<ProductDetailBO> executorCompletionService = new ExecutorCompletionService(executorService);
            Arrays.stream(productSimilarIds).forEach(id -> executorCompletionService.submit(new Task(id)));

            while (steps.get() < productSimilarIds.length) {
                steps.incrementAndGet();
                Optional.ofNullable(executorCompletionService.take().get()).ifPresent(detailBo -> similarProductsBO.getSimilarProducts().add(detailBo));
            }

            executorService.shutdownNow();

            logger.error("Returning similar products of product id: {}. [{}] ", productId, similarProductsBO);
            return similarProductsBO;

        } catch (SimilarProductNotFoundException ex) {
            logger.error("Exception raised with message: {} ", ex.getMessage());
            throw ex;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw e;
        }

    }

    private class Task implements Callable<ProductDetailBO> {
        private final String id;

        public Task(String id) {
            this.id = id;
        }

        @Override
        public ProductDetailBO call() {
            try {
                logger.error("Request product with id: {} ", id);
                ProductDetailBO product = port.getProductById(id);
                logger.error("Product returned: {} ", product);
                return product;
            } catch (Exception ex) {
                logger.error("Exception raised with message: {} ", ex.getMessage());
            }
            return null;
        }
    }
}
