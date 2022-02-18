package brokerless.sampleapp;

import brokerless.consumer.inbox.discovery.properties.ProducersConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = {"brokerless.producer.outbox.persistence", "brokerless.consumer.inbox.persistence"})
@EntityScan(basePackages = {"brokerless.producer.outbox.persistence", "brokerless.consumer.inbox.persistence"})
@SpringBootApplication(scanBasePackages = "brokerless")
@EnableConfigurationProperties(ProducersConfigurationProperties.class)
@EnableScheduling
public class BrokerlessSampleAplicationApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerlessSampleAplicationApplication.class, args);
  }

}
