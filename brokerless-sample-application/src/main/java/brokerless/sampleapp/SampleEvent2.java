package brokerless.sampleapp;

import brokerless.model.EventPayload;
import lombok.Value;

@Value
public class SampleEvent2 implements EventPayload {

  String message;
}
