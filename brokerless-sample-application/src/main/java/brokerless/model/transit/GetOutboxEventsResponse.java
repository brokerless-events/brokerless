package brokerless.model.transit;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GetOutboxEventsResponse {

  private List<SerializedEventMessage> eventMessages;
}
