package brokerless.inbox.discovery;

import java.util.List;

public interface OutboxDiscoveryStrategy {

  List<String> getProducerBaseUrls();
}
