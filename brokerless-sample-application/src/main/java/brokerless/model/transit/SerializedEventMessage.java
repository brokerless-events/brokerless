package brokerless.model.transit;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class SerializedEventMessage {
  private UUID eventId;
  private String eventType;
  private String message;
}
