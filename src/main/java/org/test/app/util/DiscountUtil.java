package org.test.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;
import org.test.app.interfaces.UserRole;

@Component
public class DiscountUtil {

	private static final BigDecimal PREMIUM_DISCOUNT = BigDecimal.valueOf(0.10); // 10%
	private static final BigDecimal HIGH_VALUE_DISCOUNT = BigDecimal.valueOf(0.05); // 5%
	private static final BigDecimal HIGH_VALUE_THRESHOLD = BigDecimal.valueOf(500);

	public BigDecimal calculateDiscount(BigDecimal orderTotal, UserRole role) {

		if (orderTotal == null || orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal discountRate = BigDecimal.ZERO;

		// Role-based discount
		if (role == UserRole.PREMIUM_USER) {
			discountRate = discountRate.add(PREMIUM_DISCOUNT);
		}

		// Order value based discount (any role)
		if (orderTotal.compareTo(HIGH_VALUE_THRESHOLD) > 0) {
			discountRate = discountRate.add(HIGH_VALUE_DISCOUNT);
		}

		return orderTotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
	}

}
