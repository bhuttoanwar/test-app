package org.test.app.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.app.dto.ApiResponse;
import org.test.app.dto.ProductDtos;
import org.test.app.service.ProductQueryService;

import java.math.BigDecimal;

/**
 * REST API for Products
 *
 * @author Anwar
 * @since 1.0.0
 */

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {

	private final ProductQueryService service;

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		ProductDtos.Response details = service.getById(id);
		ApiResponse<ProductDtos.Response> response = ApiResponse.<ProductDtos.Response>builder().code("0000")
				.message("Success").data(details).build();
		return ResponseEntity.ok(response);
	}

	// Search & Filter
	@GetMapping("/filter")
	public ResponseEntity<ApiResponse<Page<ProductDtos.Response>>> search(@RequestParam(required = false) String name,
			@RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) Boolean available, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		Page<ProductDtos.Response> result = service.search(name, minPrice, maxPrice, available, page, size, sortBy,
				sortDir);
		ApiResponse<Page<ProductDtos.Response>> response;
		if (result.isEmpty()) {
			response = ApiResponse.<Page<ProductDtos.Response>>builder().code("0000").message("Record not found")
					.build();
		} else {
			response = ApiResponse.<Page<ProductDtos.Response>>builder().code("0000")
					.message("Products fetched successfully").data(result).build();
		}

		return ResponseEntity.ok(response);
	}
}
