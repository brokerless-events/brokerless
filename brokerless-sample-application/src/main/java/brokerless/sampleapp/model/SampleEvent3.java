package brokerless.sampleapp.model;

import brokerless.model.BrokerlessEvent;
import brokerless.model.EventPayload;
import lombok.Value;

@Value
@BrokerlessEvent
public class SampleEvent3 implements EventPayload {

  String message;
  boolean throwException;
  boolean shutdownApplication;
}
