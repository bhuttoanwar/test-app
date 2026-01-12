package org.test.app.api;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.app.dto.ApiResponse;
import org.test.app.dto.UserAuthDtos;
import org.test.app.service.UserAuthQueryService;

import jakarta.validation.Valid;

/**
 * REST API for User Login and Registration
 *
 * @author Anwar
 * @since 1.0.0
 */

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthQueryController {

	private final UserAuthQueryService authService;

	public UserAuthQueryController(UserAuthQueryService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserAuthDtos.LoginRequest req) {
		String token = authService.login(req);
		Map<String, Object> additionalData = Map.of("token", token);
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder().code("0000")
				.message("Success").data(additionalData).timestamp(Instant.now()).build());

	}
}
