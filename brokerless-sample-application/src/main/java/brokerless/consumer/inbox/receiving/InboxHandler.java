package brokerless.consumer.inbox.receiving;

import brokerless.consumer.handling.invocation.EventHandlerInvoker;
import brokerless.consumer.inbox.persistence.ProducerCursorRepository;
import brokerless.model.EventMessage;
import brokerless.model.transit.SerializedEventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InboxHandler {

  private final ProducerCursorRepository producerCursorRepository;
  private final EventHandlerInvoker eventHandlerInvoker;

  @Transactional
  public void handle(ReceivedEventMessage receivedEventMessage) {
    EventMessage eventMessage = deserializeEventMessage(receivedEventMessage.getEventMessage());
    eventHandlerInvoker.invokeHandler(eventMessage);
    producerCursorRepository.updateCursor(receivedEventMessage.getProducerName(), receivedEventMessage.getEventMessage().getEventId());
  }


  @SneakyThrows
  private EventMessage deserializeEventMessage(SerializedEventMessage serializedEventMessage) {
    ObjectMapper om = new ObjectMapper();
    return om.readValue(serializedEventMessage.getMessage(), EventMessage.class);
  }
}
