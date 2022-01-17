package brokerless.outbox.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

  Page<OutboxEvent> findByTypeInAndIdAfter(Set<String> types, UUID fromIdExclusive, Pageable pageable);
}
