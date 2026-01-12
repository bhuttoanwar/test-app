package org.test.app.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Product DTOS Model
 *
 * @author Anwar
 * @since 1.0.0
 */


public class ProductDtos {

	public record CreateRequest(@NotBlank @Size(max = 120) String name, @Size(max = 2000) String description,
			@NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal price,
			@NotNull @Min(0) Integer quantity) {
	}

	public record UpdateRequest(@NotBlank @Size(max = 120) String name, @Size(max = 2000) String description,
			@NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal price,
			@NotNull @Min(0) Integer quantity) {
	}

	public record Response(Long id, String name, String description, BigDecimal price, Integer quantity,
			boolean available) {
	}
}
