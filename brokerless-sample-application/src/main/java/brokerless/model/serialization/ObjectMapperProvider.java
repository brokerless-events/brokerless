package brokerless.model.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperProvider {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public ObjectMapper get() {
    return objectMapper;
  }
}
