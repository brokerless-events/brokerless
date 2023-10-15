package brokerless.sampleapp.model;

import brokerless.model.BrokerlessEvent;
import brokerless.model.EventPayload;
import lombok.Value;

@Value
@BrokerlessEvent
public class SampleEvent4 implements EventPayload {

  public SampleEvent4(String message) {
    this.message = message;
    this.throwException = false;
    this.shutdownApplication = false;
  }

  String message;
  boolean throwException;
  boolean shutdownApplication;
}
