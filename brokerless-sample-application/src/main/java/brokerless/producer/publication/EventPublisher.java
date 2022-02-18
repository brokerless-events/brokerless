package brokerless.producer.publication;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.producer.outbox.persistence.OutboxRepositoryClient;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class EventPublisher {

  public static final String PRODUCER_INSTANCE_NAME = "p1";
  private static final UUID producerInstanceId = randomUUID();

  private final OutboxRepositoryClient outboxClient;

  public void publishEvent(EventPayload eventPayload) {

    String name = eventPayload.getClass().getName();
    EventMetadata metadata = new EventMetadata(Generators.timeBasedGenerator().generate(), name, PRODUCER_INSTANCE_NAME, producerInstanceId);
    EventProductionTracing tracing = new EventProductionTracing(now(), now());
    outboxClient.store(eventPayload, metadata, tracing);

  }
}
