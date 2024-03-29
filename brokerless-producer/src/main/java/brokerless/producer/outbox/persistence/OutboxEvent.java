package brokerless.producer.outbox.persistence;

import brokerless.model.EventMetadata;
import brokerless.model.EventTracing;
import brokerless.model.transit.TransitedEventMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Entity
@Table(name = "brokerless_outbox_events")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class OutboxEvent {

  @Id
  @Nullable
  @Column(name = "event_id")
  private UUID eventId;

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "occurred_time")
  private Instant occurredTime;

  @Column(name = "published_time")
  private Instant publishedTime;

  @Column(name = "outbox_write_time")
  private Instant outboxWriteTime;

  @Column(name = "producer_name")
  private String producerName;

  @Column(name = "producer_instance_id")
  private UUID producerInstanceId;

  @Column(name = "event_payload_serialised")
  private String eventPayloadSerialised;

  public TransitedEventMessage toSerializedEventMessage() {
    EventMetadata metadata = new EventMetadata(eventId, eventType, producerName, producerInstanceId);
    EventTracing tracing = new EventTracing(occurredTime, publishedTime, outboxWriteTime, now());
    return new TransitedEventMessage(eventPayloadSerialised, metadata, tracing);
  }
}
