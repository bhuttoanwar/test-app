package org.test.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.app.dto.UserAuthDtos;
import org.test.app.entities.User;
import org.test.app.exceptions.BadRequestException;
import org.test.app.interfaces.UserRole;
import org.test.app.repo.UserRepository;
import org.test.app.util.AppLogger;

/**
 * User Authentication Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
public class UserAuthCommandService {

	private static final AppLogger appLogger = AppLogger.getLogger(UserAuthCommandService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserAuthCommandService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void register(UserAuthDtos.RegisterRequest req) {
		appLogger.info("User Registeration Request.");
		if (userRepository.existsByUsername(req.username()))
			throw new BadRequestException("Username already exists");
		userRepository.save(User.builder().username(req.username().trim()).isActive(true)
				.passwordHash(passwordEncoder.encode(req.password())).role(UserRole.USER).build());
	}

	public void registerAdminPremiumUsers(UserAuthDtos.RegisterPremiumRequest req) {
		if (userRepository.existsByUsername(req.username()))
			throw new BadRequestException("Username already exists");
		UserRole role;
		try {
			role = UserRole.valueOf(req.role().trim().toUpperCase());
		} catch (Exception e) {
			throw new BadRequestException("Invalid role. Use USER, PREMIUM_USER, ADMIN");
		}
		userRepository.save(User.builder().username(req.username().trim()).isActive(true)
				.passwordHash(passwordEncoder.encode(req.password())).role(role).build());
	}

}
