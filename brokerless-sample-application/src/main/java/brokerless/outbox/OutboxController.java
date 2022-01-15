package brokerless.outbox;

import brokerless.model.EventMessage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OutboxController {

  private OutboxClient outboxClient;


  @GetMapping("/borkerless-events/outbox")
  public List<EventMessage> getOutboxEvents(OutboxRequest request) {
    return outboxClient.read(request.eventTypes(), request.fromCursorExclusive());
  }

}
