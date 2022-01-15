package brokerless.sampleapp;

import brokerless.model.EventPayload;
import lombok.Value;

@Value
public class SampleEvent1 implements EventPayload {

  String message;
}
