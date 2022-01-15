package brokerless.model;

import lombok.Value;

@Value
public class EventMessage {

  EventTracing tracing;
  EventMetadata metadata;
  EventPayload payload;

}
