package org.test.app.api;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.app.dto.ApiResponse;
import org.test.app.dto.UserAuthDtos;
import org.test.app.service.UserAuthCommandService;

import jakarta.validation.Valid;

/**
 * REST API for User Login and Registration
 *
 * @author Anwar
 * @since 1.0.0
 */

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthCommandController {

	private final UserAuthCommandService authService;

	public UserAuthCommandController(UserAuthCommandService authService) {
		this.authService = authService;
	}

	@PostMapping("/admin/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerPremiumUser(@Valid @RequestBody UserAuthDtos.RegisterPremiumRequest req) {
		authService.registerAdminPremiumUsers(req);

		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder().code("0000")
				.message("Premium user registered successfully").timestamp(Instant.now()).build());
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserAuthDtos.RegisterRequest req) {
		authService.register(req);
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder().code("0000")
				.message("User registered successfully").timestamp(Instant.now()).build());
	}

}
