package brokerless.producer.publication;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.producer.EventProductionTracing;
import brokerless.producer.OutboxClient;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.fasterxml.uuid.Generators;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class EventPublisher {

  public static final String PRODUCER_INSTANCE_NAME = "p1";
  private static final UUID producerInstanceId = randomUUID();

  private final OutboxClient outboxClient;

  public void publishEvent(EventPayload eventPayload) {

    String name = eventPayload.getClass().getName();
    EventMetadata metadata = new EventMetadata(Generators.timeBasedGenerator().generate(), name, now());
    EventProductionTracing tracing = new EventProductionTracing(now(), now(), PRODUCER_INSTANCE_NAME, producerInstanceId);
    outboxClient.store(eventPayload, metadata, tracing);

  }
}
