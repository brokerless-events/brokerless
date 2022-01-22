package brokerless.model.transit;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetOutboxEventsRequest {
  private Set<String> eventTypes;
  private UUID fromCursorExclusive = new UUID(0, 0);
}
