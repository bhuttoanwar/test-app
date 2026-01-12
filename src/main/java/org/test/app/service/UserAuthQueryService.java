package org.test.app.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.test.app.dto.UserAuthDtos;
import org.test.app.exceptions.BadRequestException;
import org.test.app.repo.UserRepository;
import org.test.app.util.AppUtil;

/**
 * User Authentication Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
public class UserAuthQueryService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final AppUtil appUtil;

	public UserAuthQueryService(UserRepository userRepository, AuthenticationManager authenticationManager,
			AppUtil appUtil) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.appUtil = appUtil;
	}

	public String login(UserAuthDtos.LoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		var user = userRepository.findByUsername(req.username())
				.orElseThrow(() -> new BadRequestException("Invalid credentials"));
		return appUtil.generateToken(user.getUsername(), user.getRole().name());
	}
}
