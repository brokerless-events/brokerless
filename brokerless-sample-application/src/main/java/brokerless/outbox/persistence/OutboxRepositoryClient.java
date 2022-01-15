package brokerless.outbox.persistence;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.transmission.SerializedEventMessage;
import brokerless.outbox.EventProductionTracing;
import brokerless.outbox.OutboxClient;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryClient implements OutboxClient {

  private final OutboxRepository repository;


  @Override
  public void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing) {
    OutboxEvent outboxEvent = new OutboxEvent();
    repository.save(outboxEvent);
  }


  @Override
  public List<SerializedEventMessage> read(Set<String> types, long fromCursorExclusive) {
    return null;
  }
}
