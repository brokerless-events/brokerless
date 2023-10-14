package brokerless.producer.outbox.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, UUID> {

  Page<OutboxEvent> findByEventTypeInAndEventIdAfter(Set<String> types, UUID afterEventId, Pageable pageable);
  List<OutboxEvent> findByEventIdAfter(UUID afterEventId);
  Page<OutboxEvent> findByEventTypeIn(Set<String> types, Pageable pageable);
}
