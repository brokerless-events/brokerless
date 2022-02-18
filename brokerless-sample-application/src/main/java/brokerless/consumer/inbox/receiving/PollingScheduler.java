package brokerless.consumer.inbox.receiving;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Order
@RequiredArgsConstructor
public class PollingScheduler {

  private final PollingEventsReceiver pollingEventsReceiver;

  @Scheduled(fixedRate = 3000)
  public void pollAllOutboxes() {
    pollingEventsReceiver.receive();
  }
}
