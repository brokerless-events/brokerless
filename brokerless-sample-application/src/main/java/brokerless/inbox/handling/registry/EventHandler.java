package brokerless.inbox.handling;

import brokerless.model.EventMessage;
import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.EventTracing;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;

@Slf4j
public class EventHandler<T> {

  private final TriConsumer<T, EventMetadata, EventTracing> eventHandlerFunction;
  private final Class<T> eventClass;
  private final ObjectMapper objectMapper;

  public EventHandler(final Class<T> eventClass, final TriConsumer<T, EventMetadata, EventTracing> handlerFunction, final ObjectMapper objectMapper) {
    this.eventHandlerFunction = handlerFunction;
    this.eventClass = eventClass;
    this.objectMapper = objectMapper;
  }

  public void handleEvent(EventMessage eventMessage) {
    final T payload = parsePayload(eventMessage.getPayload());
    eventHandlerFunction.accept(payload, eventMessage.getMetadata(), eventMessage.getTracing());
  }

  private T parsePayload(final EventPayload eventPayload) {
    try {
      return objectMapper.convertValue(eventPayload, eventClass);
    } catch (final IllegalArgumentException e) {
      log.error("Failed to parse events payload", e);
      return null;
    }
  }

}
