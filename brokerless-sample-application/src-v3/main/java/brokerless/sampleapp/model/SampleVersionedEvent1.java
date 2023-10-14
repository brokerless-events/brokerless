package brokerless.sampleapp.model;

import brokerless.model.BrokerlessEvent;
import brokerless.model.EventPayload;
import lombok.Value;

@Value
@BrokerlessEvent(versionMajor = 3)
public class SampleVersionedEvent1 implements EventPayload {

  String message;
  boolean throwException;
  boolean shutdownApplication;
}
