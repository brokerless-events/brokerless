package brokerless.consumer.inbox.discovery.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "brokerless.consumer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProducersConfigurationProperties {

  private List<ProducerConfiguration> producers = new ArrayList<>();

}
