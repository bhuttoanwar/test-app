package org.test.app.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.test.app.dto.ApiResponse;
import org.test.app.dto.OrderDtos;
import org.test.app.service.OrderCommandService;

/**
 * REST API for Placement of Orders
 *
 * @author Anwar
 * @since 1.0.0
 */

@RestController
@RequestMapping("/api/v1/orders")
public class OrderCommandController {

	private final OrderCommandService orderCommandService;

	public OrderCommandController(OrderCommandService orderCommandService) {
		this.orderCommandService = orderCommandService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<OrderDtos.OrderView>> place(@Valid @RequestBody OrderDtos.PlaceOrderCommand cmd,
			Authentication authentication) {
		OrderDtos.OrderView placed = orderCommandService.placeOrder(authentication.getName(), cmd);

		ApiResponse<OrderDtos.OrderView> response = ApiResponse.<OrderDtos.OrderView>builder().code("0000")
				.message("Order placed successfully").data(placed).build();

		return ResponseEntity.ok(response);
	}

}
