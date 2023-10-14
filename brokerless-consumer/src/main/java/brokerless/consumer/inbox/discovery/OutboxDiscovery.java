package brokerless.consumer.inbox.discovery;

import brokerless.consumer.inbox.discovery.properties.ProducerConfiguration;

import java.util.List;

public interface OutboxDiscovery {

  List<ProducerConfiguration> getProducerConfigurations();
}
