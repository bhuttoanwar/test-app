package org.test.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.test.app.dto.ProductDtos;
import org.test.app.entities.Product;
import org.test.app.repo.ProductRepository;
import org.test.app.util.ProductSpecs;

import java.math.BigDecimal;

/**
 * Product Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductRepository repo;

	
	public ProductDtos.Response getById(Long id) {
		// Get Product Details by ID
		Product p = repo.findById(id).filter(x -> !x.isDeleted())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		return new ProductDtos.Response(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
				p.isAvailable());
	}

	public Page<ProductDtos.Response> search(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean available,
			int page, int size, String sortBy, String sortDir) {
		Sort sort = Sort.by(sortBy == null || sortBy.isBlank() ? "id" : sortBy);
		sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();

		Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200), sort);

		Specification<Product> spec = Specification.where(ProductSpecs.notDeleted()).and(ProductSpecs.nameLike(name))
				.and(ProductSpecs.priceGte(minPrice)).and(ProductSpecs.priceLte(maxPrice))
				.and(ProductSpecs.availability(available));

		return repo.findAll(spec, pageable).map(p -> new ProductDtos.Response(p.getId(), p.getName(),
				p.getDescription(), p.getPrice(), p.getQuantity(), p.isAvailable()));
	}

}
