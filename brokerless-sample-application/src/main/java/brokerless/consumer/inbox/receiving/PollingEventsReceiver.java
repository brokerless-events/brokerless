package brokerless.consumer.inbox.receiving;

import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.consumer.inbox.configuration.ProducerConfiguration;
import brokerless.consumer.inbox.discovery.OutboxDiscovery;
import brokerless.consumer.inbox.persistence.ProducerCursorEntity;
import brokerless.consumer.inbox.persistence.ProducerCursorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class PollingEventsReceiver {

  private final OutboxPoller outboxPoller;
  private final OutboxDiscovery outboxDiscovery;
  private final InboxHandler inboxHandler;
  private final ProducerCursorRepository producerCursorRepository;
  private final EventHandlerRegistry registry;

  public void receive() {
    Map<String, UUID> cursors = producerCursorRepository.findAll().stream().collect(toMap(ProducerCursorEntity::getProducerName, ProducerCursorEntity::getCursor));
    Map<ProducerConfiguration, Optional<UUID>> producerConfigurations = outboxDiscovery.getProducerConfigurations()
        .stream()
        .collect(toMap(identity(), pc -> ofNullable(cursors.get(pc.getName()))));

    outboxPoller.poll(producerConfigurations, registry.getAllEventTypes())
        .forEach(inboxHandler::handle);


  }
}
