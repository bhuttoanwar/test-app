package org.test.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

	private String code;
	private String message;
	private T data;
	
	@Builder.Default
    private Instant timestamp = Instant.now();
}
