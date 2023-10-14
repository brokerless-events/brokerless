package brokerless.producer.publication;

import lombok.Value;

import java.time.Instant;

@Value
public class EventProductionTracing {
  Instant occurredTime;
  Instant publishedTime;

}
