package brokerless.consumer.inbox.receiving;

import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.consumer.inbox.discovery.OutboxDiscovery;
import brokerless.consumer.inbox.discovery.properties.ProducerConfiguration;
import brokerless.consumer.inbox.persistence.CursorEntity;
import brokerless.consumer.inbox.persistence.CursorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class PollingEventsReceiver {

  private final OutboxPoller outboxPoller;
  private final OutboxDiscovery outboxDiscovery;
  private final InboxHandler inboxHandler;
  private final CursorRepository cursorRepository;
  private final EventHandlerRegistry registry;

  public void receive() {
    Map<String, UUID> cursors = cursorRepository.findAll().stream().collect(toMap(CursorEntity::getProducerName, CursorEntity::getCursor));
    Map<ProducerConfiguration, Optional<UUID>> producerConfigurations = outboxDiscovery.getProducerConfigurations()
        .stream()
        .collect(toMap(identity(), pc -> ofNullable(cursors.get(pc.getName()))));

    List<ReceivedEventMessage> messages = outboxPoller.poll(producerConfigurations, registry.getAllEventTypes());
    log.debug("Polling producers with cursors: {} resulted in receiving {} messages", cursors, messages.size());
    messages.forEach(inboxHandler::handle);
  }
}
