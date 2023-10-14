package brokerless.consumer.inbox.receiving;

import brokerless.consumer.inbox.discovery.properties.ProducerConfiguration;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.GetOutboxEventsRequest;
import brokerless.model.transit.GetOutboxEventsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;

@Component
@RequiredArgsConstructor
public class OutboxApiClient {

  private final ObjectMapperProvider objectMapperProvider;

  public Stream<ReceivedEventMessage> fetchEvents(ProducerConfiguration producerConfiguration, Set<String> eventTypes, UUID afterCursor) {
    RestTemplate outboxRestTemplate = new RestTemplate();
    outboxRestTemplate.getMessageConverters().add(0, jacksonConverter());
    String url = producerConfiguration.getBaseUrl() + OUTBOX_ENDPOINT_PATH;
    GetOutboxEventsRequest request = new GetOutboxEventsRequest(eventTypes, afterCursor);
    ResponseEntity<GetOutboxEventsResponse> response = outboxRestTemplate.postForEntity(url, request, GetOutboxEventsResponse.class);

    return response.getBody().getEventMessages()
        .stream()
        .map(em -> new ReceivedEventMessage(producerConfiguration.getName(), em));
  }

  private HttpMessageConverter<?> jacksonConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapperProvider.get());
    return converter;
  }
}
