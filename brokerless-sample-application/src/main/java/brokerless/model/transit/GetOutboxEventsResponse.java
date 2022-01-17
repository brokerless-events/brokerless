package brokerless.model.transit;

import lombok.Value;

import java.util.List;

@Value
public class GetOutboxEventsResponse {

  List<SerializedEventMessage> eventMessages;
}
