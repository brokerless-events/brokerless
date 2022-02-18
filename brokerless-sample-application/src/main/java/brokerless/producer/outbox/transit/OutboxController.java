package brokerless.producer.outbox.transit;

import brokerless.model.transit.GetOutboxEventsRequest;
import brokerless.model.transit.GetOutboxEventsResponse;
import brokerless.model.transit.TransitedEventMessage;
import brokerless.producer.outbox.persistence.OutboxRepositoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OutboxController {

  private final OutboxRepositoryClient outboxRepositoryClient;

  @PostMapping(value = OUTBOX_ENDPOINT_PATH, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  public GetOutboxEventsResponse getOutboxEvents(@RequestBody GetOutboxEventsRequest request) {
    List<TransitedEventMessage> messages = outboxRepositoryClient.read(request.getEventTypes(), request.getAfterCursor());
    log.debug("Responding to outbox request for event types {} after cursor {} with {} messages nd cursor {}",
        request.getEventTypes(),
        request.getAfterCursor(),
        messages.size(),
        messages.isEmpty() ? "<none>" : messages.get(messages.size()-1).getEventMetadata().getEventId());
    return new GetOutboxEventsResponse(messages);
  }

}
