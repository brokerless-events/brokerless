package brokerless.producer;

import brokerless.TestSpringConfiguration;
import brokerless.model.EventPayload;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.GetOutboxEventsResponse;
import brokerless.model.transit.TransitedEventMessage;
import brokerless.producer.outbox.persistence.OutboxJpaRepository;
import brokerless.producer.outbox.persistence.OutboxRepositoryClient;
import brokerless.producer.publication.EventPublisher;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static brokerless.model.transit.TransitConstants.OUTBOX_ENDPOINT_PATH;
import static java.lang.String.format;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
//@AutoConfigureTestEntityManager
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
//@TestPropertySource(properties = {
//    "logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG",
//    "spring.profiles.active=dev"
//})
//@ContextConfiguration(classes = ProducerIntegrationTest.SpringConfiguration.class)

@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.config.location=classpath:persistence.yaml",
    "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///brokerless?TC_REUSABLE=true",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
    "spring.flyway.cleanDisabled=false"
})
@ContextConfiguration(classes = ProducerIntegrationTest.SpringConfiguration.class)
class ProducerIntegrationTest {

  @Autowired EventPublisher publisher;
  @Autowired OutboxRepositoryClient outboxClient;
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapperProvider objectMapperProvider;
  @Autowired OutboxJpaRepository repository;

  @Test
  @SneakyThrows
  @Transactional
  void should_publish_and_serve_events() {
    List<EventPayload> events = of(
      new SampleProducerTest1Event("testdata1"),
      new SampleProducerTest2Event("testdata2"),
      new SampleProducerTest3Event("testdata3"),
      new SampleProducerTest4Event("testdata4"));

    events.forEach(publisher::publishEvent);
    assertThat(repository.findAll().size()).isGreaterThan(0);

    String responseString = mockMvc.perform(post(OUTBOX_ENDPOINT_PATH)
            .contentType("application/json")
            .characterEncoding("UTF-8")
            .accept("application/json")
            .content(format(""" 
            {
             "eventTypes": ["%s", "%s", "%s", "%s"]
            }
            """,SampleProducerTest1Event.class.getName(),
                SampleProducerTest2Event.class.getName(),
                SampleProducerTest3Event.class.getName(),
                SampleProducerTest4Event.class.getName())
            ))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<TransitedEventMessage> eventMessages = objectMapperProvider.get().readValue(responseString, GetOutboxEventsResponse.class).getEventMessages();

    assertThat(eventMessages).hasSize(4);
    assertEventMessage(eventMessages.get(0), SampleProducerTest1Event.class, "testdata1");
    assertEventMessage(eventMessages.get(1), SampleProducerTest2Event.class, "testdata2");
    assertEventMessage(eventMessages.get(2), SampleProducerTest3Event.class, "testdata3");
    assertEventMessage(eventMessages.get(3), SampleProducerTest4Event.class, "testdata4");
  }

  private <T> void assertEventMessage(TransitedEventMessage eventMessage, Class<T> payloadClass, String expectedData) {
    assertThat(eventMessage.getEventMetadata().getEventType()).isEqualTo(payloadClass.getName());
    assertThat(getEventPayloadData(eventMessage.getEventPayloadSerialised(), payloadClass)).isEqualTo(expectedData);
  }

  @SneakyThrows
  private <T> String getEventPayloadData(String message, Class<T> payloadClass) {
    T t = objectMapperProvider.get().readValue(message, payloadClass);
    if (!payloadClass.isAssignableFrom(t.getClass())) {
      throw new IllegalArgumentException("Unexpected payload class. Expected: " + payloadClass + " . Actual: " + t.getClass());
    }
    if (t instanceof SampleProducerTest1Event) {
      return ((SampleProducerTest1Event) t).getData1();
    }
    if (t instanceof SampleProducerTest2Event) {
      return ((SampleProducerTest2Event) t).getData2();
    }
    if (t instanceof SampleProducerTest3Event) {
      return ((SampleProducerTest3Event) t).getData3();
    }
    if (t instanceof SampleProducerTest4Event) {
      return ((SampleProducerTest4Event) t).getData4();
    }
    throw new IllegalArgumentException("Event payload class not supported: " + payloadClass);
  }

  @Test
  void should_publish_and_serve_events_in_pages() throws Exception {
    List<SampleProducerTest1Event> events = IntStream.range(0, 1001).boxed().map(i -> new SampleProducerTest1Event("testdata" + i)).toList();

    events.forEach(publisher::publishEvent);

    String responseStringPage1 = mockMvc.perform(post(OUTBOX_ENDPOINT_PATH)
            .contentType("application/json")
            .characterEncoding("UTF-8")
            .accept("application/json")
            .content(format(""" 
            {
             "eventTypes": ["%s"]
            }
            """, SampleProducerTest1Event.class.getName())
            ))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<TransitedEventMessage> eventMessagesPage1 = objectMapperProvider.get().readValue(responseStringPage1, GetOutboxEventsResponse.class).getEventMessages();

    assertThat(eventMessagesPage1).hasSize(1000);
    for (int i = 0 ; i < 1000 ; i ++) {
      assertEventMessage(eventMessagesPage1.get(i), SampleProducerTest1Event.class, "testdata" + i);
    }

    UUID lastEventMessageId = eventMessagesPage1.get(999).getEventMetadata().getEventId();

    String responseStringPage2 = mockMvc.perform(post(OUTBOX_ENDPOINT_PATH)
            .contentType("application/json")
            .characterEncoding("UTF-8")
            .accept("application/json")
            .content(format(""" 
            {
             "eventTypes": ["%s"],
             "afterCursor": "%s"
            }
            """, SampleProducerTest1Event.class.getName(), lastEventMessageId)
            ))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<TransitedEventMessage> eventMessages2 = objectMapperProvider.get().readValue(responseStringPage2, GetOutboxEventsResponse.class).getEventMessages();

    assertThat(eventMessages2).hasSize(1);
    assertEventMessage(eventMessages2.get(0), SampleProducerTest1Event.class, "testdata1000");
  }


  @EnableWebMvc
  @EnableTransactionManagement
  @EnableJpaRepositories("brokerless.producer.outbox.persistence")
  @ComponentScan(basePackages = {"brokerless.producer", "brokerless.model"})
  @EntityScan(basePackages = "brokerless.producer.outbox.persistence")
  @Import(TestSpringConfiguration.class)
  static class SpringConfiguration {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
      CommonsRequestLoggingFilter filter
          = new CommonsRequestLoggingFilter();
      filter.setIncludeQueryString(true);
      filter.setIncludePayload(true);
      filter.setMaxPayloadLength(10000);
      filter.setIncludeHeaders(false);
      filter.setAfterMessagePrefix("REQUEST DATA: ");
      return filter;
    }
  }
}
