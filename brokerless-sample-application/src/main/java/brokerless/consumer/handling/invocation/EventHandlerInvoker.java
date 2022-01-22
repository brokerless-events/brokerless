package brokerless.consumer.handling.invocation;

import brokerless.consumer.handling.registry.EventHandler;
import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.model.transit.TransitedEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerInvoker {

  private final EventHandlerRegistry registry;

  public void invokeHandler(TransitedEventMessage message) {
    Optional<EventHandler<?>> handler = registry.get(message.getEventMetadata().getEventType());
    if (handler.isPresent()) {
      handler.get().handleEvent(message);
    } else {
      log.warn("No event handler found for received event of type {}", message.getEventMetadata().getEventType());
    }
  }

}
