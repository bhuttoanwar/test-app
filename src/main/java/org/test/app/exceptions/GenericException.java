package org.test.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.test.app.dto.ApiResponse;
import org.test.app.service.UserAuthCommandService;
import org.test.app.util.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic Exception Handler to handle exceptions over all project
 *
 * @author Anwar
 * @since 1.0.0
 */

@ControllerAdvice
public class GenericException {
	private static final AppLogger appLogger = AppLogger.getLogger(UserAuthCommandService.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("4041").message(ex.getMessage()).data(null)
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler({ BadRequestException.class, InsufficientStockException.class })
	public ResponseEntity<ApiResponse<Void>> handleBadRequest(RuntimeException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("4001").message(ex.getMessage()).data(null)
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		for (var err : ex.getBindingResult().getAllErrors()) {
			String field = err instanceof FieldError fe ? fe.getField() : err.getObjectName();
			errors.put(field, err.getDefaultMessage());
		}

		ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder().code("4002")
				.message("Validation failed").data(errors).build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
		appLogger.error("Unhandled error", ex);

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("5000").message("Internal server error")
				.data(null).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(UsernameNotFoundException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("0001").message("User Not Found.").data(null)
				.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("0002").message("Application state error")
				.data(null).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("0003").message("Illegal arguments error.")
				.data(null).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().code("0004").message("Invalid credentials.")
				.data(null).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
