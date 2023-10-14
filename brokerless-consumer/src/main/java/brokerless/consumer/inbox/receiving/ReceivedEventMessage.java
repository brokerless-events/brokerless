package brokerless.consumer.inbox.receiving;

import brokerless.model.transit.TransitedEventMessage;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ReceivedEventMessage {

  private String producerName;
  private TransitedEventMessage message;
}
