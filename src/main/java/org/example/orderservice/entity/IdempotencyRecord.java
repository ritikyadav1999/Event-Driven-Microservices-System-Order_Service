package org.example.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "idempotency_records",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_user_idempotency",
                columnNames = {"user_id","idempotency_key"}
        )
})
@NoArgsConstructor
@Setter
@Getter
public class IdempotencyRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(nullable = false,name = "user_id")
    private UUID userId;

    @NotBlank
    @Column(nullable = false,name = "idempotency_key",length = 100)
    private String idempotencyKey;

    @NotBlank
    @Column(nullable = false , name = "request_hash",length = 64)
    private String requestHash;

    @NotNull
    @Column(nullable = false ,name = "order_id")
    private UUID orderId;

    @Column(nullable = false,name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

}
