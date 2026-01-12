package org.test.app.util;

import org.springframework.data.jpa.domain.Specification;
import org.test.app.entities.Product;

import java.math.BigDecimal;


/**
 * Supporting Class for Searching of Products
 *
 *
 * @author Anwar
 * @since 1.0.0
 */

public final class ProductSpecs {


	public static Specification<Product> notDeleted() {
		return (root, q, cb) -> cb.isFalse(root.get("deleted"));
	}

	public static Specification<Product> nameLike(String name) {
		if (name == null || name.trim().isEmpty())
			return null;
		String like = "%" + name.trim().toLowerCase() + "%";
		return (root, q, cb) -> cb.like(cb.lower(root.get("name")), like);
	}

	public static Specification<Product> priceGte(BigDecimal min) {
		if (min == null)
			return null;
		return (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("price"), min);
	}

	public static Specification<Product> priceLte(BigDecimal max) {
		if (max == null)
			return null;
		return (root, q, cb) -> cb.lessThanOrEqualTo(root.get("price"), max);
	}

	// availability: true => quantity > 0 ; false => quantity <= 0
	public static Specification<Product> availability(Boolean available) {
		if (available == null)
			return null;
		return (root, q, cb) -> available ? cb.greaterThan(root.get("quantity"), 0)
				: cb.lessThanOrEqualTo(root.get("quantity"), 0);
	}
}
