package org.test.app.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * For Logging the Service Request and Response.
 *
 * @author Anwar
 * @since 1.0.0
 */


@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

	private static final AppLogger appLogger = AppLogger.getLogger(RequestResponseLoggingFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		long startTime = System.currentTimeMillis();

		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {

			long duration = System.currentTimeMillis() - startTime;

			logRequest(requestWrapper);
			logResponse(responseWrapper, duration);

			responseWrapper.copyBodyToResponse();
		}
	}
	
	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        //Do not Log h2 Console Request Responses.
        return path.startsWith("/app/db-admin-concole");
    }


	private void logRequest(ContentCachingRequestWrapper request) {
		String body = getContent(request.getContentAsByteArray());

		appLogger.info("""
				>>> REQUEST
				Method : {}
				URI    : {}
				Query  : {}
				Body   : {}
				""", request.getMethod(), request.getRequestURI(), request.getQueryString(), maskSensitiveData(body));
	}

	private void logResponse(ContentCachingResponseWrapper response, long duration) {

		String body = getContent(response.getContentAsByteArray());

		appLogger.info("""
				<<< RESPONSE
				Status  : {}
				Time    : {} ms
				Body    : {}
				""", response.getStatus(), duration, maskSensitiveData(body));
	}

	private String getContent(byte[] buf) {
		if (buf == null || buf.length == 0)
			return "";
		return new String(buf, StandardCharsets.UTF_8);
	}

	private String maskSensitiveData(String body) {
		if (body == null)
			return null;

		return body.replaceAll("(?i)\"password\"\\s*:\\s*\".*?\"", "\"password\":\"***\"")
				.replaceAll("(?i)\"token\"\\s*:\\s*\".*?\"", "\"token\":\"***\"");
	}
}
