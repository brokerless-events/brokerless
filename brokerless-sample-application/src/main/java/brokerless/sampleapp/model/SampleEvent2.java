package brokerless.sampleapp.model;

import brokerless.model.BrokerlessEvent;
import brokerless.model.EventPayload;
import lombok.Value;

@Value
@BrokerlessEvent
public class SampleEvent2 implements EventPayload {

  String message;
}