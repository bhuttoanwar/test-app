package org.test.app.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.test.app.security.CustomUserDetailsService;
import org.test.app.util.AppLogger;
import org.test.app.util.AppUtil;

import java.io.IOException;

/**
 * JWT Authentication Filer uses to handle each request 
 * 
 * @author Anwar
 * @since 1.0.0
 */

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final AppLogger appLogger = AppLogger.getLogger(JwtAuthFilter.class);

	private final AppUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthFilter(AppUtil jwtUtil,CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		appLogger.debug("Filtering request with Token: " + auth);
		if (auth != null && auth.startsWith("Bearer ")) {
			String token = auth.substring(7);
			appLogger.info("Validating User Request.");
			try {
				String userName = jwtUtil.extractUsername(token);
				appLogger.info("User Name :" + userName);
				if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
					var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception e) {
				appLogger.info("Something went wrong.");
				appLogger.debug("JWT invalid: {}", e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().startsWith("/db-admin-concole");
	}
}
