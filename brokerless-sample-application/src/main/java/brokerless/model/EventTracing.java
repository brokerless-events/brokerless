package brokerless.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class EventTracing {

  public EventTracing(Instant occurredTime, Instant publishedTime) {
    this.occurredTime = occurredTime;
    this.publishedTime = publishedTime;
    this.outboxTime = null;
    this.inboxTime = null;
    this.handledTime = null;
  }

  Instant occurredTime;
  Instant publishedTime;
  Instant outboxTime;
  Instant inboxTime;
  Instant handledTime;
}
