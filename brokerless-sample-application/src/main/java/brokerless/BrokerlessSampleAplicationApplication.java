package brokerless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"brokerless.outbox.persistence", "brokerless.inbox.persistence"})
@SpringBootApplication(scanBasePackages = "brokerless")
@EnableConfigurationProperties
public class BrokerlessSampleAplicationApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerlessSampleAplicationApplication.class, args);
  }

}
