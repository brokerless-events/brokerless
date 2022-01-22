package brokerless.model;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EventTracing {

  public EventTracing(Instant occurredTime, Instant publishedTime) {
    this(occurredTime, publishedTime, null);
  }

  public EventTracing(Instant occurredTime, Instant publishedTime, Instant outboxWriteTime) {
    this(occurredTime, publishedTime, outboxWriteTime,null);
  }

  public EventTracing(Instant occurredTime, Instant publishedTime, Instant outboxWriteTime, Instant outboxServedTime) {
    this.occurredTime = occurredTime;
    this.publishedTime = publishedTime;
    this.outboxWriteTime = outboxWriteTime;
    this.outboxServedTime = outboxServedTime;
    this.inboxReceivedTime = null;
    this.handledTime = null;
  }

  Instant occurredTime;
  Instant publishedTime;
  Instant outboxWriteTime;
  Instant outboxServedTime;
  Instant inboxReceivedTime;
  Instant handledTime;
}
