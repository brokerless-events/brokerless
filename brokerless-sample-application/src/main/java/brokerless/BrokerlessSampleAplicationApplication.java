package brokerless;

import brokerless.consumer.ProducersConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"brokerless.producer.outbox", "brokerless.consumer.persistence"})
@SpringBootApplication(scanBasePackages = "brokerless")
@EnableConfigurationProperties(ProducersConfigurationProperties.class)
public class BrokerlessSampleAplicationApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerlessSampleAplicationApplication.class, args);
  }

}
