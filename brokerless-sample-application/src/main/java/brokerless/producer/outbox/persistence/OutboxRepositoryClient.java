package brokerless.producer.outbox.persistence;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.transit.SerializedEventMessage;
import brokerless.producer.EventProductionTracing;
import brokerless.producer.OutboxClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryClient implements OutboxClient {

  private final OutboxRepository repository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @SneakyThrows
  public void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing) {
    String message = objectMapper.writeValueAsString(payload);
    OutboxEvent outboxEvent = new OutboxEvent(metadata.getEventId(), metadata.getEventType(), tracing.getPublishedTime(), message);
    repository.save(outboxEvent);
  }


  @Override
  public List<SerializedEventMessage> read(Set<String> types, UUID fromCursorExclusive) {
    PageRequest pageable = PageRequest.of(0, 1000, Sort.by(ASC, "event_id"));
    return repository.findByTypeInAndIdAfter(types, fromCursorExclusive, pageable)
        .stream()
        .map(OutboxEvent::toSerializedEventMessage)
        .toList();
  }
}
