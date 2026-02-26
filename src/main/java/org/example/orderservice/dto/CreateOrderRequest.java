package org.example.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(

        @NotBlank
        @Length(min = 1, max = 100)
        String customerName,

        @NotNull
        UUID userId,

        @NotNull
        BigDecimal orderAmount
) {
}
