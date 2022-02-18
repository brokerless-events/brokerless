package brokerless.consumer.inbox.receiving;

import brokerless.consumer.handling.invocation.EventHandlerInvoker;
import brokerless.consumer.inbox.persistence.CursorEntity;
import brokerless.consumer.inbox.persistence.CursorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboxHandler {

  private final CursorRepository cursorRepository;
  private final EventHandlerInvoker eventHandlerInvoker;

  @Transactional
  public void handle(ReceivedEventMessage message) {
    eventHandlerInvoker.invokeHandler(message.getMessage());
    CursorEntity cursor = new CursorEntity(message.getProducerName(), message.getMessage().getEventMetadata().getEventId());
    cursorRepository.save(cursor);
    log.debug("Handled event type {}, stored cursor {} for producer {}",
        message.getMessage().getEventMetadata().getEventType(),
        cursor.getCursor(),
        cursor.getProducerName());
  }


}
