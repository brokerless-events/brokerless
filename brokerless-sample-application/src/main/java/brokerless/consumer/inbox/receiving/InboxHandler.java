package brokerless.consumer.inbox.receiving;

import brokerless.consumer.handling.invocation.EventHandlerInvoker;
import brokerless.consumer.inbox.persistence.ProducerCursorEntity;
import brokerless.consumer.inbox.persistence.ProducerCursorRepository;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.TransitedEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InboxHandler {

  private final ProducerCursorRepository producerCursorRepository;
  private final EventHandlerInvoker eventHandlerInvoker;

  @Transactional
  public void handle(ReceivedEventMessage message) {
    eventHandlerInvoker.invokeHandler(message.getMessage());
    producerCursorRepository.save(new ProducerCursorEntity(message.getProducerName(), message.getMessage().getEventMetadata().getEventId()));
  }


}
