package brokerless.inbox.discovery;

import brokerless.inbox.InboxConfigurationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ConfigurationPropertyOutboxDiscoveryStrategy implements OutboxDiscoveryStrategy {

  private final InboxConfigurationProperties inboxProperties;

  @Override
  public List<String> getProducerBaseUrls() {
    return inboxProperties.getProducersBaseUrl();
  }
}
