package brokerless;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

//@SpringBootTest
//@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
//@AutoConfigureTestEntityManager
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
//@TestPropertySource(properties = {
//    "logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG",
//    "spring.profiles.active=dev"
//})
//@ContextConfiguration(classes = ProducerIntegrationTest.SpringConfiguration.class)

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.config.location=classpath:persistence.yaml",
    "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///brokerless?TC_REUSABLE=true",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
    "spring.flyway.cleanDisabled=false"
})
@ContextConfiguration(classes = ContextTest.SpringConfiguration.class)
class ContextTest {

  @Autowired
  MockMvc mockMvc;
  @Test
  void contextLoads() {
    assertThat(true).isTrue();
  }

  @EnableWebMvc
  @EnableTransactionManagement
  @EnableJpaRepositories("brokerless.producer.outbox.persistence")
  @EntityScan(basePackages = "brokerless.producer.outbox.persistence")
  @ComponentScan(basePackages = {"brokerless.producer", "brokerless.model"})
  static class SpringConfiguration {

  }
}
