package brokerless.consumer.inbox.discovery.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "brokerless.consumer")
@Component
@AllArgsConstructor
@Data
public class ProducersConfigurationProperties {

  private List<ProducerConfiguration> producers = new ArrayList<>();

}
