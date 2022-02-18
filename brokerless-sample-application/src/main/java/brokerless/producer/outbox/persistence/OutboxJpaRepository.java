package brokerless.producer.outbox.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, UUID> {

  Page<OutboxEvent> findByEventTypeInAndEventIdAfter(Set<String> types, UUID fromIdExclusive, Pageable pageable);
}