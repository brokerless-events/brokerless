package brokerless.inbox.receiving;

import brokerless.inbox.ProducerConfiguration;
import brokerless.inbox.discovery.OutboxDiscovery;
import brokerless.inbox.persistence.ProducerCursorRepository;
import brokerless.model.transit.GetOutboxEventsResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;

@Component
@AllArgsConstructor
public class OutboxPoller {

  private final OutboxDiscovery outboxDiscovery;

  @Scheduled(fixedRate = 1000)
  public void pollAllOutboxes() {
    poll(outboxDiscovery.getProducerConfigurations());
  }

  public List<ReceivedEventMessage> poll(List<ProducerConfiguration> producerConfigurations) {
    return producerConfigurations
        .stream()
        .map(pc -> fetchEvents(pc.getName(), pc.getBaseUrl()))
        .flatMap(Function.identity())
        .sorted((e1, e2) -> e1.getEventMessage().getEventId().compareTo(e2.getEventMessage().getEventId()))
        .toList();
  }

  private Stream<ReceivedEventMessage> fetchEvents(String producerName, String producerBaseUrl) {
    RestTemplate outboxRestTemplate = new RestTemplate();
    ResponseEntity<GetOutboxEventsResponse> response = outboxRestTemplate.getForEntity(producerBaseUrl + OUTBOX_ENDPOINT_PATH, GetOutboxEventsResponse.class);

    return response.getBody().getEventMessages()
        .stream()
        .map(em -> new ReceivedEventMessage(em, producerName));
  }

}
