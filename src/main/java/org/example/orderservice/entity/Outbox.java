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
@Setter
@Getter
@NoArgsConstructor
@Table(name = "outbox",
    indexes = {
        @Index( name = "idx_outbox_published_at",columnList = "published_at")
    }
)
public class Outbox {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true,name = "event_id")
    private UUID eventId;

    @Column(name = "aggregate_type",nullable = false,length = 50)
    private String aggregateType;

    @Column(name = "aggregate_id",nullable = false)
    private UUID aggregateId;

    @NotBlank
    @Column(name = "event_type",nullable = false,length = 100)
    private String eventType;

    @NotBlank
    @Column(name = "payload",columnDefinition = "TEXT",nullable = false)
    private String payload;

    @Column(name = "created_at",nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(nullable = true,name = "published_at")
    private Instant publishedAt;


}
