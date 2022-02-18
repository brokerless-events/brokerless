package brokerless.model.transit;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetOutboxEventsRequest {
  private Set<String> eventTypes = new HashSet<>();
  private UUID afterCursor = new UUID(0, 0);
}
