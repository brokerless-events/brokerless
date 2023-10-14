package brokerless.consumer.inbox.receiving;

import brokerless.consumer.inbox.discovery.properties.ProducerConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Component
@AllArgsConstructor
public class OutboxPoller {

  private final OutboxApiClient outboxApiClient;

  public List<ReceivedEventMessage> poll(Map<ProducerConfiguration, Optional<UUID>> producerBaseUrlToCursor, Set<String> eventTypes) {
    return producerBaseUrlToCursor.entrySet()
        .stream()
        .flatMap(entry -> receive(eventTypes, entry))
        .sorted(comparing(e -> e.getMessage().getEventMetadata().getEventId()))
        .toList();
  }

  private Stream<ReceivedEventMessage> receive(Set<String> eventTypes, Map.Entry<ProducerConfiguration, Optional<UUID>> e) {
    UUID afterCursor = e.getValue().orElse(new UUID(0, 0));
    return outboxApiClient.fetchEvents(e.getKey(), eventTypes, afterCursor);
  }

}
