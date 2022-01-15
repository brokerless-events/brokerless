package brokerless.inbox;

import brokerless.model.EventPayload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class EventHandlerRegistry {

  private final Map<String, Set<Consumer>> handlers = new HashMap<>();


  public <T extends EventPayload> void register(Class<T> eventType, Consumer<T> newHandler) {
    String typeName = eventType.getName();
    if (!handlers.containsKey(typeName)) {
      handlers.put(typeName, new HashSet<>());
    }
    handlers.get(typeName).add(newHandler);

  }
}
