package brokerless.consumer.handling.registry;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.EventTracing;
import brokerless.model.serialization.ObjectMapperProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandlerRegistry {

  private final ConcurrentHashMap<String, EventHandler<?>> handlers = new ConcurrentHashMap<>();

  ObjectMapperProvider objectMapperProvider;


  public <T extends EventPayload> void register(Class<T> eventClass, TriConsumer<T, EventMetadata, EventTracing> handlerFunction) {
    String eventType = eventClass.getName();
    EventHandler<?> handler = new EventHandler<>(eventClass, handlerFunction, objectMapperProvider.get());
    final EventHandler<?> previousHandler = handlers.put(eventType, handler);
    log.info("Registered event handler for event type: '{}'", eventType);
    if (previousHandler != null) {
      log.warn("The previously registered event handler for event type {} has been replaced with another handler. " +
          "Please define single handler per event type to obtain predictable behaviour.", eventType);
    }
  }

  public Optional<EventHandler<?>> get(String type) {
    return ofNullable(handlers.get(type));
  }

}
