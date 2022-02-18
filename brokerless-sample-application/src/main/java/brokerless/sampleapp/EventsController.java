package brokerless.sampleapp;

import brokerless.consumer.handling.BrokerlessEventHandler;
import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.model.EventPayload;
import brokerless.producer.publication.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventsController {

  private final EventHandlerRegistry eventHandlerRegistry;
  private final EventPublisher eventPublisher;
  private final List<EventPayload> events = new ArrayList<>();

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

  @BrokerlessEventHandler
  public void handleSampleEvent1(SampleEvent1 event) {
    log.info("Handle: " + event.getMessage());
  }

  @BrokerlessEventHandler
  public void handleSampleEvent2(SampleEvent2 event) {
    log.info("Handle: " + event.getMessage());
  }


}
