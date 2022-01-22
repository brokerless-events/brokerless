package brokerless.consumer.inbox.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerCursorRepository extends JpaRepository<ProducerCursorEntity, String> {

}
