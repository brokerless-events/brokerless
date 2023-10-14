package brokerless.consumer.handling.registry;

import brokerless.model.EventMetadata;
import brokerless.model.EventTracing;
import brokerless.model.transit.TransitedEventMessage;
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

  public void handleEvent(TransitedEventMessage message) {
    final T payload = parsePayload(message.getEventPayloadSerialised());
    eventHandlerFunction.accept(payload, message.getEventMetadata(), message.getEventTracing());
  }

  private T parsePayload(final String payloadSerialised) {
    try {
      return objectMapper.convertValue(payloadSerialised, eventClass);
    } catch (final IllegalArgumentException e) {
      log.error("Failed to parse events payload", e);
      return null;
    }
  }

}
