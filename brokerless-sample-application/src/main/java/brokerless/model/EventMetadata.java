package brokerless.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EventMetadata {

  public EventMetadata(UUID eventId, String eventType) {
    this.eventId = eventId;
    this.eventType = eventType;
  }

  UUID eventId;
  String eventType;
  String producerName;
  UUID producerInstanceId;

}
