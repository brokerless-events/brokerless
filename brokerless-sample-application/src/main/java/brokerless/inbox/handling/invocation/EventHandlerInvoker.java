package brokerless.inbox.handling;

import brokerless.model.EventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandlerInvoker {

  private final EventHandlerRegistry registry;

  public void invokeHandler(EventMessage eventMessage) {
    Optional<EventHandler<?>> handler = registry.get(eventMessage.getMetadata().getEventType());
    if (handler.isPresent()) {
      handler.get().handleEvent(eventMessage);
    }
  }

}
