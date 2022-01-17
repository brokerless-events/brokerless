package brokerless.consumer.discovery;

import brokerless.consumer.ProducerConfiguration;
import brokerless.consumer.ProducersConfigurationProperties;
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
