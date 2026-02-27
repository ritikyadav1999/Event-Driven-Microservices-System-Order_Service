package org.example.orderservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreateEvent(
        UUID eventId,
        UUID orderId,
        UUID userId,
        BigDecimal amount,
        String eventType,
        Instant occuredAt
) {
}
