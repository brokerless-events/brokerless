package brokerless.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "borkerless.inbox")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InboxConfigurationProperties {

  private List<String> outboxUrls;
}
