package org.test.app.exceptions;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BadRequestException(String m) {
		super(m);
	}
}
