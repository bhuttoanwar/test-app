package org.test.app.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.app.dto.ApiResponse;
import org.test.app.dto.ProductDtos;
import org.test.app.service.ProductCommandService;
import java.net.URI;

/**
 * REST API for Products
 *
 * @author Anwar
 * @since 1.0.0
 */

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {

	private final ProductCommandService service;

	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody ProductDtos.CreateRequest req) {
		ProductDtos.Response created = service.create(req);
		ApiResponse<ProductDtos.Response> response = ApiResponse.<ProductDtos.Response>builder().code("0000")
				.message("Product created successfully").data(created).build();
		return ResponseEntity.created(URI.create("/products/" + created.id())).body(response);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDtos.UpdateRequest req) {
		ProductDtos.Response update = service.update(id, req);
		ApiResponse<ProductDtos.Response> response = ApiResponse.<ProductDtos.Response>builder().code("0000")
				.message("Update Successfull.").data(update).build();
		return ResponseEntity.ok(response);
	}

	// Soft delete
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.softDelete(id);
		ApiResponse<ProductDtos.Response> response = ApiResponse.<ProductDtos.Response>builder().code("0000")
				.message("Product Deleted.").build();
		return ResponseEntity.ok(response);
	}

}
