package brokerless.model.transmission;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class SerializedEventMessage {
  UUID id;
  String type;
  Instant publishedTime;
  String message;
}
