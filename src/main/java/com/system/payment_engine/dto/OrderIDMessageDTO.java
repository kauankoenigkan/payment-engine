package com.system.payment_engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public record OrderIDMessageDTO(
        @JsonProperty("orderId") Long orderId
) implements Serializable {}