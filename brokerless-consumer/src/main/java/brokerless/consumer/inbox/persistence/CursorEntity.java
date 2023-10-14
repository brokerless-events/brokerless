package brokerless.consumer.inbox.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "brokerless_cursors")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class CursorEntity {

  @Id
  @Column(name = "producer_name")
  private String producerName;

  @Column(name = "cursor")
  @Nullable
  private UUID cursor;

}
