package org.example.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Outbox;
import org.example.orderservice.repository.OutboxRepo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepo outboxRepo;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional
    @Scheduled(fixedDelay = 20000)
    public void publish(){
        List<Outbox> outboxes = outboxRepo.fetchBatchForPublishing(50);
        for (Outbox event : outboxes) {

            Map<String,String> map = Map.of(
                    "eventId",event.getEventId().toString(),
                    "eventType",event.getEventType(),
                    "aggregateType","ORDER",
                    "aggregateId",event.getAggregateId().toString(),
                    "payload",event.getPayload()
            );

            redisTemplate.opsForStream()
                    .add("order-events",map);

            event.setPublishedAt(Instant.now());
        }

    }

}
