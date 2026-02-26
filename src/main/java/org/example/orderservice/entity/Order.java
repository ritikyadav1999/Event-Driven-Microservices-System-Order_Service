package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID orderId;

    @Column(length = 100 ,nullable = false)
    private String customerName;

    @Column(nullable = false)
    private BigDecimal orderAmount;

    @Column(nullable = false)
    private UUID userId;      // giving fake userId

    @Column(nullable = false)
    @CreationTimestamp
    private Instant createAt;

    private Instant updateAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
