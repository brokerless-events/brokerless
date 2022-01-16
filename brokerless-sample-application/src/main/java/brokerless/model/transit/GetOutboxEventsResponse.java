package brokerless.model.transit;

import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class GetOutboxResponse {

  List<SerializedEventMessage> eventMessages;
}
