package brokerless.outbox.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import brokerless.model.transit.SerializedEventMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "BROKERLESS_OUTBOX_EVENTS")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class OutboxEvent {

  @Id
  @Nullable
  @Column(name = "event_id")
  private UUID id;

  @Column(name = "event_type")
  private String type;

  @Column(name = "published_time")
  private Instant publishedTime;

  @Column(name = "event_message")
  private String message;

  public SerializedEventMessage toSerializedEventMessage() {
    return new SerializedEventMessage(id, type, message);
  }
}
