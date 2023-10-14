package brokerless.producer.outbox.persistence;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.TransitedEventMessage;
import brokerless.producer.publication.EventProductionTracing;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
@RequiredArgsConstructor
public class OutboxJpaRepositoryClient implements OutboxRepositoryClient {

  private final OutboxJpaRepository repository;
  private final ObjectMapperProvider objectMapperProvider;

  @Override
  @SneakyThrows
  public void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing) {
    String payloadSerialised = objectMapperProvider.get().writeValueAsString(payload);
    OutboxEvent outboxEvent = new OutboxEvent(metadata.getEventId(),
        metadata.getEventType(),
        tracing.getOccurredTime(),
        tracing.getPublishedTime(),
        Instant.now(),
        metadata.getProducerName(),
        metadata.getProducerInstanceId(),
        payloadSerialised);
    repository.save(outboxEvent);
  }

  @Override
  public List<TransitedEventMessage> read(Set<String> types, UUID afterCursor) {
    PageRequest pageable = PageRequest.of(0, 1000, Sort.by(ASC, "eventId"));
    return repository.findByEventTypeInAndEventIdAfter(types, afterCursor, pageable)
        .stream()
        .map(OutboxEvent::toSerializedEventMessage)
        .toList();
  }
}
