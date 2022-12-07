package com.capitole.challenge.functional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0, stubs = {"classpath:/wiremock/stub_definitions"}, files = {"classpath:/wiremock/stub_responses"})
abstract class ControllerTest {
}
