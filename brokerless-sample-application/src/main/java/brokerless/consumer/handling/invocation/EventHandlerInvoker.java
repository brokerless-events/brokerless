package brokerless.inbox.handling.invocation;

import brokerless.inbox.handling.registry.EventHandler;
import brokerless.inbox.handling.registry.EventHandlerRegistry;
import brokerless.model.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerInvoker {

  private final EventHandlerRegistry registry;

  public void invokeHandler(EventMessage eventMessage) {
    Optional<EventHandler<?>> handler = registry.get(eventMessage.getMetadata().getEventType());
    if (handler.isPresent()) {
      handler.get().handleEvent(eventMessage);
    } else {
      log.warn("No event handler found for received event of type {}", eventMessage.getMetadata().getEventType());
    }
  }

}
