package brokerless.consumer.inbox;

import brokerless.consumer.handling.invocation.EventHandlerInvoker;
import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.consumer.inbox.configuration.ProducerConfiguration;
import brokerless.consumer.inbox.configuration.ProducersConfigurationProperties;
import brokerless.consumer.inbox.receiving.PollingEventsReceiver;
import brokerless.consumer.inbox.trigger.ScheduledTrigger;
import brokerless.model.EventMessage;
import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.EventTracing;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.GetOutboxEventsRequest;
import brokerless.model.transit.GetOutboxEventsResponse;
import brokerless.model.transit.TransitedEventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.Instant.ofEpochSecond;
import static java.util.Set.of;
import static java.util.UUID.fromString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WireMockTest(httpPort = 53685)
@DataJpaTest
@ContextConfiguration
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class ConsumerInboxIntegrationTest {

  private static final String PRODUCER_NAME = "producer1";
  private static final UUID EVENT_ID_1 = fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID EVENT_ID_2 = fromString("22222222-2222-2222-2222-222222222222");
  private static final UUID EVENT_ID_3 = fromString("33333333-3333-3333-3333-333333333333");
  private static final Instant OCCURRED_TIME = ofEpochSecond(1641031061L);
  private static final Instant PUBLISHED_TIME = OCCURRED_TIME.plusSeconds(1);
  private static final Instant OUTBOX_TIME = OCCURRED_TIME.plusSeconds(1);
  private static final Instant INBOX_TIME = OCCURRED_TIME.plusSeconds(1);
  private static final Instant HANDLED_TIME = OCCURRED_TIME.plusSeconds(1);
  @Autowired
  PollingEventsReceiver pollingEventsReceiver;
  @Autowired EventHandlerInvoker handlerInvoker;
  @Autowired ObjectMapperProvider objectMapperProvider;

  private static WireMockRuntimeInfo wireMockRuntimeInfo;

  @BeforeEach
  void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
    ConsumerInboxIntegrationTest.wireMockRuntimeInfo = wireMockRuntimeInfo;
  }

  @Test
  @SneakyThrows
  void should_poll_and_handle() {
    TransitedEventMessage transitedEventMessage = createTransitedEventMessage(EVENT_ID_1);
    stubOutbox(transitedEventMessage, null);

    pollingEventsReceiver.receive();

    verify(handlerInvoker).invokeHandler(eq(transitedEventMessage));
  }

  @Test
  @SneakyThrows
  void should_poll_with_cursor() {
    stubOutbox(createTransitedEventMessage(EVENT_ID_1), null);
    pollingEventsReceiver.receive();
    TransitedEventMessage transitedEventMessage2 = createTransitedEventMessage(EVENT_ID_2);
    stubOutbox(transitedEventMessage2, EVENT_ID_1);

    pollingEventsReceiver.receive();

    verify(handlerInvoker).invokeHandler(eq(transitedEventMessage2));

    TransitedEventMessage transitedEventMessage3 = createTransitedEventMessage(EVENT_ID_3);
    stubOutbox(transitedEventMessage3, EVENT_ID_2);

    pollingEventsReceiver.receive();

    verify(handlerInvoker).invokeHandler(eq(transitedEventMessage3));
  }

  private void stubOutbox(TransitedEventMessage transitedEventMessage, UUID cursor) throws JsonProcessingException {
    String body = objectMapperProvider.get().writeValueAsString(new GetOutboxEventsResponse(List.of(transitedEventMessage)));
    String request = objectMapperProvider.get().writeValueAsString(new GetOutboxEventsRequest(of(Test1Event.class.getName()), cursor));
    removeAllMappings();
    stubFor(post(OUTBOX_ENDPOINT_PATH).withRequestBody(equalToJson(request, true, true))
        .willReturn(aResponse().withStatus(200).withBody(body).withHeader("Content-Type", "application/json")));
  }

  @NotNull
  private TransitedEventMessage createTransitedEventMessage(UUID eventId) throws JsonProcessingException {
    String eventPayloadSerialised = objectMapperProvider.get().writeValueAsString(new Test1Event());
    EventMetadata eventMetadata = new EventMetadata(eventId, Test1Event.class.getName());
    EventTracing eventTracing = new EventTracing(OCCURRED_TIME, PUBLISHED_TIME);
    return new TransitedEventMessage(eventPayloadSerialised, eventMetadata, eventTracing);
  }

  @Configuration
  @ComponentScan(basePackages = {"brokerless.consumer.inbox", "brokerless.model"})
  @EnableJpaRepositories("brokerless.consumer.inbox.persistence")
  @EntityScan("brokerless.consumer.inbox.persistence")
  public static class SpringConfiguration {
    @Bean
    public EventHandlerInvoker eventHandlerInvoker() {
      return mock(EventHandlerInvoker.class);
    }

    @Bean
    public EventHandlerRegistry eventHandlerRegistry() {
      EventHandlerRegistry mock = mock(EventHandlerRegistry.class);
      when(mock.getAllEventTypes()).thenReturn(of(Test1Event.class.getName()));
      return mock;
    }

    @Bean
    public ScheduledTrigger scheduledTrigger() {
      return mock(ScheduledTrigger.class);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ProducersConfigurationProperties producersConfigurationProperties() {
      List<ProducerConfiguration> producers = List.of(new ProducerConfiguration(PRODUCER_NAME, "http://127.0.0.1:53685"));
      return new ProducersConfigurationProperties(producers);
    }
  }


  private static class Test1Event implements EventPayload {

  }
}