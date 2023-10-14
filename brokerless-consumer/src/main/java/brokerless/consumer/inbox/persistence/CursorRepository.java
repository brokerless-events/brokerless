package brokerless.consumer.inbox.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursorRepository extends JpaRepository<CursorEntity, String> {

}
