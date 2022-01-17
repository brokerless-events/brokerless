package brokerless.inbox.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProducerCursorRepository extends JpaRepository<ProducerCursor, String> {

  @Modifying
  @Query("UPDATE ProducerCursor pc set pc.cursor = ?2 where pc.producerName = ?1")
  void updateCursor(String producerName, UUID cursor);
}
