package brokerless.sampleapp;

import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.model.EventPayload;
import brokerless.producer.publication.EventPublisher;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventsController {

  private final EventHandlerRegistry eventHandlerRegistry;
  private final EventPublisher eventPublisher;
  private final List<EventPayload> events = new ArrayList<>();

  @PostConstruct
  public void registerEventHandler() {
    eventHandlerRegistry.register(SampleEvent1.class, this::handleSampleEvent);
    eventHandlerRegistry.register(SampleEvent2.class, this::handleSampleEvent);
  }

  @PostMapping("/publish")
  public void publish() {
    eventPublisher.publishEvent(new SampleEvent1("message1"));
    eventPublisher.publishEvent(new SampleEvent2("message2"));
  }

  @GetMapping("/received")
  @ResponseBody
  public List<EventPayload> getReceived() {
    return events;
  }

  public void handleSampleEvent(SampleEvent1 event) {
    log.info(event.getMessage());
  }

  public void handleSampleEvent(SampleEvent2 event) {
    log.info(event.getMessage());
  }


}
