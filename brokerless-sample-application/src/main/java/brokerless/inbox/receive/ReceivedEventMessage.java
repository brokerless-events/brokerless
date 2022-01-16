package brokerless.inbox.receiving;

import brokerless.model.transit.SerializedEventMessage;
import lombok.Value;

@Value
public class ReceivedEventMessage {

  SerializedEventMessage eventMessage;
  String producerName;
}
