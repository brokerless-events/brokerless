package brokerless.consumer.inbox.discovery;

import brokerless.consumer.inbox.configuration.ProducerConfiguration;
import brokerless.consumer.inbox.configuration.ProducersConfigurationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ConfigurationPropertyOutboxDiscovery implements OutboxDiscovery {

  private final ProducersConfigurationProperties producersConfigurationProperties;

  @Override
  public List<ProducerConfiguration> getProducerConfigurations() {
    return producersConfigurationProperties.getProducers();
  }
}
