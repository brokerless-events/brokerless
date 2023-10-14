package brokerless.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EventMessage {

  EventPayload payload;
  EventMetadata metadata;
  EventTracing tracing;

}
