package brokerless.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EventMessage {

  EventPayload payload;
  EventMetadata metadata;
  EventTracing tracing;

}
