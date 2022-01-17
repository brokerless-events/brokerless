package brokerless.consumer.inbox.discovery;

import brokerless.consumer.ProducerConfiguration;

import java.util.List;

public interface OutboxDiscovery {

  List<ProducerConfiguration> getProducerConfigurations();
}
