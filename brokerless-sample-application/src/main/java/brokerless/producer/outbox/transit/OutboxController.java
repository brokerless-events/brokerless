package brokerless.outbox.transit;

import brokerless.model.transit.GetOutboxEventsRequest;
import brokerless.model.transit.SerializedEventMessage;
import brokerless.outbox.OutboxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;

@RestController
@RequiredArgsConstructor
public class OutboxController {

  private OutboxClient outboxClient;

  @GetMapping(OUTBOX_ENDPOINT_PATH)
  public List<SerializedEventMessage> getOutboxEvents(GetOutboxEventsRequest request) {
    return outboxClient.read(request.eventTypes(), request.fromCursorExclusive());
  }

}
