package brokerless.model.transit;

import brokerless.model.EventMetadata;
import brokerless.model.EventTracing;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TransitedEventMessage {
  private String eventPayloadSerialised;
  private EventMetadata eventMetadata;
  private EventTracing eventTracing;
}
