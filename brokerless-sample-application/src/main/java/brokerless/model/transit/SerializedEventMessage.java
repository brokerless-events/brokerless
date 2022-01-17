package brokerless.model.transit;

import lombok.Value;

import java.util.UUID;

@Value
public class SerializedEventMessage {
  UUID eventId;
  String eventType;
  String message;
}
