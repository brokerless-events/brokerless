package brokerless.producer.outbox.transit;

import brokerless.model.transit.GetOutboxEventsRequest;
import brokerless.model.transit.GetOutboxEventsResponse;
import brokerless.producer.outbox.persistence.OutboxRepositoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;

@RestController
@RequiredArgsConstructor
public class OutboxController {

  private final OutboxRepositoryClient outboxRepositoryClient;

  @PostMapping(value = OUTBOX_ENDPOINT_PATH, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  public GetOutboxEventsResponse getOutboxEvents(@RequestBody GetOutboxEventsRequest request) {
    return new GetOutboxEventsResponse(outboxRepositoryClient.read(request.getEventTypes(), request.getFromCursorExclusive()));
  }

}
