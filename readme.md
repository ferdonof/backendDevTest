# Development notes

The original readme was renamed
to [readme_original.md](https://github.com/ferdonof/backendDevTest/blob/resilience4j-approach/readme_original.md)

For this project I developed a spring boot application and the solution uses different approaches, each one in different
branches.
The original repository files remains in main branch.
I created a [development](https://github.com/ferdonof/backendDevTest/tree/development) branch with default values of the
server for request
time a read time.
Development branch was forked in two additional branches.
The [feign-approach](https://github.com/ferdonof/backendDevTest/tree/feign-approach) preserves the files from
development files,
but connection-timeout and read-timeout where set to 100ms and 60000ms respectively, according to the expected
request/response times.

```
feign:
  okhttp:
    enable: true
  client:
    config:
      default:
        connectTimeout: 100
        readTimeout: 60000
```

By the other hand, I
created [resilience4j-approach](https://github.com/ferdonof/backendDevTest/tree/resilience4j-approach) branch, with a
resilience4j approach. I didn't worked with
resilience4j before, but after a bit of research and hands-on I think the goal was achieved. However, I think
this could be better having more research.
The resilience4j configuration is this:

```
resilience4j.circuitbreaker:
  configs:
    default:
      registerHealthIndicator: true
      # Failure rate threshold percentage
      failureRateThreshold: 25
      # Minimum number of call attempts before rate threshold percentage is checked.
      ringBufferSizeInClosedState: 10
      # How long to wait until switching to half open.
      waitDurationInOpenState: 3s
      # Number of successful requests before moving back to closed from half open.
      ringBufferSizeInHalfOpenState: 5
      # Exceptions that do not count towards opening the circuit.
      ignoreExceptions:
        # Ignore 4xx exceptions.
        - org.springframework.web.client.HttpClientErrorException
        - feign.FeignException.FeignClientException

  instances:
    get-product-similar-ids:
      baseConfig: default
    get-product:
      baseConfig: default

resilience4j.timelimiter:
  instances:
    default:
      timeoutDuration: 500ms
      cancelRunningFuture: true
```

# About business and other project rules

I decided to consider the next business and development rules because I think I was free to choose:

- If a request for the external api takes too long, I return a ProductDetail with the ID, and a message in its name.
  This will be considered specially for resilience4j-approach.
- In feign-approach, I decided to set connection timeout to 100ms, and read timeout to 60s (over the most expensive
  request).
- I stated to develop the use case functionality in a synchronous way (as you can see in revision 7735f74a), then I
  replace it with a threaded way to request for product details.

# Application architecture

I developed the application using clean architecture. You can find the business logic in the business package. The other
packages are for infrastructure classes.

# Results

I executed each branch and compare the results for each approach.
The next image show how the execution results was. From left to right, it shows the results for development,
feign-approach and resilience4j-approach respectively.
![Results](./assets/results/results.png "Development, feign-approach and resilience4j-aproach results")

## Development branch

![development](./assets/results/development.png "Development execution")

![development-results](./assets/results/results-dev.png "Development results")

## Feign-approach branch

![feign](./assets/results/feign.png "Feign execution")

![feign-results](./assets/results/results-feign.png "Feign results")

## Resilience4j-approach branch

![resilience4j](./assets/results/resilience4j.png "Resilience4j execution")

![resilience4j-results](./assets/results/results-resilience4j.png "Resilience4j results")

## Summary

| **Branch**                | **http_reqs** | **avg iteration_duration** | **iterations** | **avg http_req_waiting** |
|---------------------------|:-------------:|---------------------------:|----------------|:------------------------:|
| **development**           |     1709      |                       4.9s | 1638           |          4.31s           |
| **feign-approach**        |     1464      |                      5.29s | 1417           |           4.7s           |
| **resilience4j-approach** |     13541     |                   754.82ms | 13185          |         247.55ms         |

# Tests

I tried to create functional and unit test for each branch, but for reasons of time I didn't develop integration tests.
In fact, I removed ExistentApiPortTest from resilient4j-approach because it didn't work without including circuit
breaker, and I didn't have time to investigate how to write them.
Functional and unit tests were tuned to work in each branch.
