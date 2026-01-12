package org.test.app.exceptions;

public class InsufficientStockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InsufficientStockException(String m) {
		super(m);
	}
}
