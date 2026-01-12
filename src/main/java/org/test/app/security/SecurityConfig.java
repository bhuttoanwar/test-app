package org.test.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.test.app.security.jwt.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());

		// 261001 @Anwar Added For H2 Viewer
		http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

		// 261001 @Anwar Commented because of h2 Console was creating problem.
		// http.httpBasic(Customizer.withDefaults());

		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth

				// For H2 Console
				.requestMatchers("/db-admin-concole/**").permitAll()
				// For Login AUthentication Service
				// .requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/api/v1/auth/login").permitAll().requestMatchers("/api/v1/auth/register").permitAll()
				.requestMatchers("/api/v1/auth/admin/**").authenticated()

				// For Spring Actuators
				.requestMatchers("/actuator/**").permitAll()

				// For OPEN API
				.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

				// For Product Management
				.requestMatchers(HttpMethod.POST, "/products/create**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("ADMIN", "USER", "PREMIUM_USER")

				// For Orders
				.requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("USER", "PREMIUM_USER", "ADMIN")

				.anyRequest().authenticated());
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// @Anwar Password Encoader Bean
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder encoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(encoder);
		return new ProviderManager(provider);
	}

}
