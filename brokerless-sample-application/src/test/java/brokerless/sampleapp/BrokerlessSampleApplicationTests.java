package brokerless.sampleapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-unittest.properties", properties = {
    "spring.profiles.active=dev",
    "spring.config.location=classpath:persistence.yaml",
    "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///brokerless?TC_REUSABLE=true",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
    "spring.flyway.cleanDisabled=false"
})
class BrokerlessSampleApplicationTests {

  @Test
  void contextLoads() {
  }

}
