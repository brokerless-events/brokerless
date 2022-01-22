package brokerless.consumer.inbox.discovery;

import brokerless.consumer.inbox.configuration.ProducerConfiguration;

import java.util.List;

public interface OutboxDiscovery {

  List<ProducerConfiguration> getProducerConfigurations();
}
