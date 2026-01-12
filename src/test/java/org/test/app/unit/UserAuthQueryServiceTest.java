package org.test.app.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.test.app.dto.UserAuthDtos.LoginRequest;
import org.test.app.entities.User;
import org.test.app.exceptions.BadRequestException;
import org.test.app.interfaces.UserRole;
import org.test.app.repo.UserRepository;
import org.test.app.service.UserAuthQueryService;
import org.test.app.util.AppUtil;

@ExtendWith(MockitoExtension.class)
class UserAuthQueryServiceTest {

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	UserRepository userRepository;

	@Mock
	AppUtil appUtil;

	@InjectMocks
	UserAuthQueryService service;

	@Test
	void login_shouldThrowException_whenUserNotFound() {

		// Arrange
		LoginRequest req = new LoginRequest("anwar", "123");

		when(authenticationManager.authenticate(any())).thenReturn(null);

		when(userRepository.findByUsername("anwar")).thenReturn(Optional.empty());

		// Act + Assert
		assertThrows(BadRequestException.class, () -> service.login(req));
	}

	@Test
	void login_shouldSuccess_whenUserFound() {

		// Arrange
		LoginRequest req = new LoginRequest("admin", "admin123");

		User user = User.builder().username("admin").role(UserRole.ADMIN).build();

		when(authenticationManager.authenticate(any())).thenReturn(null);

		when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

		when(appUtil.generateToken("admin", "ADMIN")).thenReturn("jwt-token");

		// Act
		String token = service.login(req);

		// Assert
		assertNotNull(token);
		assertEquals("jwt-token", token);
	}
}
