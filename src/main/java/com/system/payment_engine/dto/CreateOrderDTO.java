package com.system.payment_engine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderDTO(
        @NotNull(message = "O ID do produto é obrigatório") Long productId,
        @NotNull(message = "A quantidade é obrigatória") @Min(1) Integer quantity
) {}