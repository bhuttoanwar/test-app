package org.test.app.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * User Login and Registration DTOS Model
 *
 * @author Anwar
 * @since 1.0.0
 */

public class UserAuthDtos {
	public record RegisterPremiumRequest(@NotBlank String username, @NotBlank String password,@NotBlank String role) {}
	public record RegisterRequest(@NotBlank String username, @NotBlank String password) {}
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record AuthResponse(String token) {}
}
