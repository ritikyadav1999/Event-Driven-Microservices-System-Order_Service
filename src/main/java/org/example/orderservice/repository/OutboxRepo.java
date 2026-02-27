package org.example.orderservice.repository;

import org.example.orderservice.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxRepo extends JpaRepository<Outbox, UUID> {

    @Query(
            value = """
                    SELECT * FROM outbox
                    WHERE published_at IS NULL
                    ORDER BY created_at
                    LIMIT :batchSize
                    FOR UPDATE SKIP LOCKED 
""", nativeQuery = true
    )
    List<Outbox> fetchBatchForPublishing(@Param("batchSize") int batchSize);

}
