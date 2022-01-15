package brokerless.outbox.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
}
