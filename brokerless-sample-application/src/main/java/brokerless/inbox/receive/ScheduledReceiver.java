package brokerless.inbox.receiving;

import brokerless.inbox.discovery.OutboxDiscovery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledReceiver {

  private final OutboxPoller outboxPoller;
  private final OutboxDiscovery outboxDiscovery;
  private final InboxHandler inboxHandler;

  @Scheduled(fixedRate = 1000)
  public void pollAllOutboxes() {
    outboxPoller.poll(outboxDiscovery.getProducerConfigurations())
        .forEach(inboxHandler::handle);


  }
}
