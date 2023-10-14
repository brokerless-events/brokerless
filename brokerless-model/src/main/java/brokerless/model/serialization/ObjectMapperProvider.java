package brokerless.model.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperProvider {

  private final ObjectMapper objectMapper;

  public ObjectMapperProvider() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  public ObjectMapper get() {
    return objectMapper;
  }
}
