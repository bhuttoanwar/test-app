package org.test.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order DTOS Model
 *
 * @author Anwar
 * @since 1.0.0
 */

public class OrderDtos {

    // Command input
    public record OrderItemRequest(@NotNull Long productId, @Min(1) int quantity) {}
    public record PlaceOrderCommand(@NotEmpty @Valid List<OrderItemRequest> items) {}

    // Read model (response)
    public record OrderItemView(
            Long productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal discountApplied,
            BigDecimal totalPrice
    ) {}

    public record OrderView(
            Long orderId,
            BigDecimal orderTotal,
            BigDecimal discountTotal,
            List<OrderItemView> items
    ) {}
}
