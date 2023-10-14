package brokerless.consumer.handling;

import brokerless.consumer.handling.discovery.EventHandlerDiscoveryProcessor;
import brokerless.consumer.handling.invocation.EventHandlerInvoker;
import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.EventTracing;
import brokerless.model.serialization.ObjectMapperProvider;
import brokerless.model.transit.TransitedEventMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.time.Instant.ofEpochSecond;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ConsumerHandlingIntegrationTest {
  private static final Instant OCCURRED_TIME = ofEpochSecond(1641031061L);
  private static final Instant PUBLISHED_TIME = OCCURRED_TIME.plusSeconds(1);
  private static final Instant OUTBOX_WRITE_TIME = PUBLISHED_TIME.plusSeconds(1);
  private static final Instant OUTBOX_SERVED_TIME = OUTBOX_WRITE_TIME.plusSeconds(1);
  private static final Instant INBOX_RECEIVED_TIME = OUTBOX_SERVED_TIME.plusSeconds(1);
  private static final Instant HANDLED_TIME = INBOX_RECEIVED_TIME.plusSeconds(1);
  public static final UUID EVENT_ID = randomUUID();

  private static List<Class<?>> sideEffects;

  @BeforeEach
  void setUp() {
    sideEffects = new ArrayList<>();
  }

  @ParameterizedTest
  @ValueSource(classes = {
      CorrectWithSingleHandlerAndOneArg.class,
      CorrectWithSingleHandlerAndTwoArgs.class,
      CorrectWithSingleHandlerAndThreeArgs.class,
      CorrectMultipleHandlers.class
  })
  void should_register_single_handler_with_one_args(Class<?> handlerClass) {
    final AnnotationConfigApplicationContext context = createWith(handlerClass);

    checkSideEffects(context, createEventMessage(new Test1Event(), context), handlerClass);
  }

  @Test
  void should_register_handlers_for_different_messages() {
    final AnnotationConfigApplicationContext context = createWith(CorrectWithDifferentMessages.class);

    checkSideEffects(context, createEventMessage(new Test1Event(), context), CorrectWithDifferentMessages.class);
    sideEffects.clear();
    checkSideEffects(context, createEventMessage(new Test2Event(), context), CorrectWithDifferentMessages.class);
  }


  @ParameterizedTest
  @ValueSource(classes = {NoMethodVisibility.class, IllegalArgumentsCount.class, IllegalArgumentType.class, IllegalReturnType.class})
  void should_not_register_handler(Class<?> handler) {
    assertThatExceptionOfType(BeanCreationException.class)
        .isThrownBy(() -> createWith(handler));
  }

  @SneakyThrows
  private static TransitedEventMessage createEventMessage(EventPayload eventPayload, AnnotationConfigApplicationContext context) {
    EventMetadata metadata = new EventMetadata(EVENT_ID, eventPayload.getClass().getName());
    EventTracing tracing = new EventTracing(OCCURRED_TIME, PUBLISHED_TIME, OUTBOX_WRITE_TIME, OUTBOX_SERVED_TIME, INBOX_RECEIVED_TIME, HANDLED_TIME);
    ObjectMapperProvider objectMapperProvider = context.getBean(ObjectMapperProvider.class);
    return new TransitedEventMessage(objectMapperProvider.get().writeValueAsString(eventPayload), metadata, tracing);
  }


  private AnnotationConfigApplicationContext createWith(final Class<?>... beans) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("brokerless.consumer.handling", "brokerless.model");
    context.register(SpringConfiguration.class);
    context.register(beans);
    context.refresh();
    return context;
  }

  private <T> void checkSideEffects(AnnotationConfigApplicationContext context, TransitedEventMessage message, Class<?>... expectedSideEffects) {
    EventHandlerInvoker invoker = context.getBean(EventHandlerInvoker.class);
    invoker.invokeHandler(message);
    assertThat(sideEffects).containsOnly(expectedSideEffects);
  }

  @Configuration
  public static class SpringConfiguration {

    @Bean
    public EventHandlerDiscoveryProcessor eventHandlerDiscoveryProcessor(EventHandlerRegistry registry) {
      return new EventHandlerDiscoveryProcessor(registry);
    }
  }

  public static class CorrectWithSingleHandlerAndOneArg {

    @BrokerlessEventHandler
    public void handler(Test1Event payload) {
      sideEffects.add(this.getClass());
    }
  }
  public static class CorrectWithSingleHandlerAndTwoArgs {

    @BrokerlessEventHandler
    public void handler(Test1Event payload, EventMetadata metadata) {
      assertThat(metadata.getEventId()).isEqualTo(EVENT_ID);
      assertThat(metadata.getEventType()).isEqualTo(Test1Event.class.getName());
      sideEffects.add(this.getClass());
    }
  }

  public static class CorrectWithSingleHandlerAndThreeArgs {

    @BrokerlessEventHandler
    public void handler(Test1Event payload, EventMetadata metadata, EventTracing tracing) {
      assertThat(tracing.getOccurredTime()).isEqualTo(OCCURRED_TIME);
      assertThat(tracing.getPublishedTime()).isEqualTo(PUBLISHED_TIME);
      assertThat(tracing.getOutboxWriteTime()).isEqualTo(OUTBOX_WRITE_TIME);
      assertThat(tracing.getOutboxServedTime()).isEqualTo(OUTBOX_SERVED_TIME);
      assertThat(tracing.getInboxReceivedTime()).isEqualTo(INBOX_RECEIVED_TIME);
      assertThat(tracing.getHandledTime()).isEqualTo(HANDLED_TIME);
      sideEffects.add(this.getClass());
    }
  }

  public static class CorrectMultipleHandlers {

    @BrokerlessEventHandler
    public void handlerA(Test1Event payload) {
      sideEffects.add(this.getClass());
    }

    @BrokerlessEventHandler
    public void handlerB(Test1Event payload) {
      sideEffects.add(this.getClass());
    }
  }

  public static class CorrectWithDifferentMessages {

    @BrokerlessEventHandler
    public void handlerA(Test1Event payload) {
      sideEffects.add(this.getClass());
    }

    @BrokerlessEventHandler
    public void handlerB(Test2Event payload) {
      sideEffects.add(this.getClass());
    }
  }

  public static class NoMethodVisibility {

    @BrokerlessEventHandler
    private void handler(Test1Event payload) {
    }
  }

  public static class IllegalArgumentsCount {

    @BrokerlessEventHandler
    public void handler(Test1Event payload, Object invalidArgument) {
    }
  }

  public static class IllegalArgumentType {

    @BrokerlessEventHandler
    public void handler(String payload) {
    }
  }

  public static class IllegalReturnType {

    @BrokerlessEventHandler
    public String handler(Test1Event payload) {
      return null;
    }
  }

  private static class Test1Event implements EventPayload {

  }
  private static class Test2Event implements EventPayload {

  }

}