package brokerless.model.transit;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GetOutboxEventsResponse {

  private List<TransitedEventMessage> eventMessages;
}
