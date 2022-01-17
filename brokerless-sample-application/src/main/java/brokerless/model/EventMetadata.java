package brokerless.model;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class EventMetadata {

  UUID eventId;
  String eventType;
  Instant occurredTime;

}
