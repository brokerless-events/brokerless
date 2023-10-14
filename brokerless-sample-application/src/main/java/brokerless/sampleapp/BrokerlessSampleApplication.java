package brokerless.sampleapp;

import brokerless.consumer.inbox.discovery.properties.ProducersConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = {"brokerless.producer", "brokerless.consumer", "brokerless.sampleapp"})
@EntityScan(basePackages = {"brokerless.producer", "brokerless.consumer", "brokerless.sampleapp"})
@SpringBootApplication(scanBasePackages = {"brokerless.producer", "brokerless.consumer", "brokerless.sampleapp", "brokerless.model"})
@EnableConfigurationProperties(ProducersConfigurationProperties.class)
@EnableScheduling
public class BrokerlessSampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerlessSampleApplication.class, args);
  }

}
