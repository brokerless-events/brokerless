package brokerless.consumer.inbox.trigger;

import brokerless.consumer.inbox.receiving.PollingEventsReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Order
@RequiredArgsConstructor
public class ScheduledTrigger {

  private final PollingEventsReceiver pollingEventsReceiver;

  @Scheduled(fixedRate = 1000)
  public void pollAllOutboxes() {
    pollingEventsReceiver.receive();
  }
}
