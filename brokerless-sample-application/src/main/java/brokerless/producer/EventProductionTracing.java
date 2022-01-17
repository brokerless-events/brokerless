package brokerless.producer;

import brokerless.model.EventTracing;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class EventProductionTracing {
  Instant occurredTime;
  Instant publishedTime;
  String producerInstanceName;
  UUID producerInstanceId;

  public EventTracing toEventTracing() {
    return new EventTracing(occurredTime, publishedTime);
  }
}
