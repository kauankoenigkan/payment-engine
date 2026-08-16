package com.system.payment_engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;

public record OrderNotificationDTO(
        @JsonProperty("orderId")
        Long orderId,

        @JsonProperty("productId")
        Long productId,

        @JsonProperty("productName")
        String productName,

        @JsonProperty("totalAmount")
        BigDecimal totalAmount,

        @JsonProperty("status")
        String status
) implements Serializable {}