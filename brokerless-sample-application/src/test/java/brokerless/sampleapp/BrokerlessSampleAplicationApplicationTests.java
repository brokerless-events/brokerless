package brokerless.sampleapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-unittest.properties")
class BrokerlessSampleAplicationApplicationTests {

  @Test
  void contextLoads() {
  }

}
