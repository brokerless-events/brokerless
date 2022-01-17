package brokerless.inbox.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "BROKERLESS_PRODUCER_CURSORS")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class ProducerCursor {

  @Id
  @Column(name = "producer_name")
  private String producerName;

  @Column(name = "cursor")
  @Nullable
  private UUID cursor;

}
