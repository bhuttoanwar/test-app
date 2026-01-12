package org.test.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.app.dto.OrderDtos;
import org.test.app.entities.Order;
import org.test.app.entities.Product;
import org.test.app.entities.User;
import org.test.app.exceptions.BadRequestException;
import org.test.app.exceptions.InsufficientStockException;
import org.test.app.exceptions.NotFoundException;
import org.test.app.repo.OrderRepository;
import org.test.app.repo.ProductRepository;
import org.test.app.repo.UserRepository;
import org.test.app.util.DiscountUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Order Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
public class OrderCommandService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final DiscountUtil discountUtil;

	public OrderCommandService(OrderRepository orderRepository, ProductRepository productRepository,
			UserRepository userRepository, DiscountUtil discountUtil) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.discountUtil = discountUtil;
	}

	@Transactional
	public OrderDtos.OrderView placeOrder(String username, OrderDtos.PlaceOrderCommand cmd) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

		if (cmd.items() == null || cmd.items().isEmpty())
			throw new BadRequestException("Order items are required");

		Set<Long> productIds = cmd.items().stream().map(OrderDtos.OrderItemRequest::productId)
				.collect(Collectors.toSet());
		Map<Long, Product> products = productRepository.findAllById(productIds).stream()
				.collect(Collectors.toMap(Product::getId, p -> p));

		// validating stock
		for (var item : cmd.items()) {
			Product p = products.get(item.productId());
			if (p == null)
				throw new NotFoundException("Product not found: " + item.productId());
			if (item.quantity() <= 0)
				throw new BadRequestException("Quantity must be >= 1 for product " + item.productId());
			if (p.getQuantity() < item.quantity())
				throw new InsufficientStockException("Insufficient stock for product " + p.getId());
		}

		// For Calculating total amount
		BigDecimal subtotal = BigDecimal.ZERO;
		for (var item : cmd.items()) {
			Product p = products.get(item.productId());
			subtotal = subtotal.add(p.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
		}
		subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

		// Proceeding with Discount if any
		BigDecimal discountTotal = discountUtil.calculateDiscount(subtotal, user.getRole());
		if (discountTotal.compareTo(subtotal) > 0)
			discountTotal = subtotal;

		BigDecimal total = subtotal.subtract(discountTotal).setScale(2, RoundingMode.HALF_UP);

		Order order = Order.builder().user(user).orderTotal(total).discountTotal(discountTotal).build();

		// decrease inventory
		for (var item : cmd.items()) {
			Product p = products.get(item.productId());
			p.setQuantity(p.getQuantity() - item.quantity());
		}
		productRepository.saveAll(products.values());

		order = orderRepository.save(order);

		var itemViews = order.getItems().stream().map(oi -> new OrderDtos.OrderItemView(oi.getProduct().getId(),
				oi.getQuantity(), oi.getUnitPrice(), oi.getDiscountApplied(), oi.getTotalPrice())).toList();

		return new OrderDtos.OrderView(order.getId(), order.getOrderTotal(), order.getDiscountTotal(), itemViews);
	}
}
