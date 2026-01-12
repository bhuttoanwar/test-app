package org.test.app.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.test.app.repo.UserRepository;

import java.util.List;


/**
 * Implementation of Spring Security User Authentication Custom
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
	}
}
