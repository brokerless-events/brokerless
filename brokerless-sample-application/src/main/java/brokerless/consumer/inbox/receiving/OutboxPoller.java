package brokerless.consumer.inbox.receiving;

import brokerless.consumer.inbox.discovery.properties.ProducerConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Comparator.comparing;

@Component
@AllArgsConstructor
public class OutboxPoller {

  private final OutboxApiClient outboxApiClient;

  public List<ReceivedEventMessage> poll(Map<ProducerConfiguration, Optional<UUID>> producerBaseUrlToCursor, Set<String> eventTypes) {
    return producerBaseUrlToCursor.entrySet()
        .stream()
        .flatMap(e -> outboxApiClient.fetchEvents(e.getKey(), eventTypes, e.getValue().orElse(new UUID(0, 0))))
        .sorted(comparing(e -> e.getMessage().getEventMetadata().getEventId()))
        .toList();
  }

}
