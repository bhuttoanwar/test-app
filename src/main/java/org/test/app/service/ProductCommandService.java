package org.test.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.app.dto.ProductDtos;
import org.test.app.entities.Product;
import org.test.app.repo.ProductRepository;

/**
 * Product Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class ProductCommandService {

	private final ProductRepository repo;

	@Transactional
	public ProductDtos.Response create(ProductDtos.CreateRequest req) {

		// Check if Product Exists.
		if (repo.existsByNameIgnoreCaseAndDeletedFalse(req.name())) {
			throw new IllegalArgumentException("Product name already exists");
		}

		Product p = Product.builder().name(req.name().trim())
				.description(req.description() == null ? null : req.description().trim()).price(req.price())
				.quantity(req.quantity()).deleted(false).build();

		repo.save(p);
		return new ProductDtos.Response(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
				p.isAvailable());
	}

	@Transactional
	public ProductDtos.Response update(Long id, ProductDtos.UpdateRequest req) {
		Product p = repo.findById(id).filter(x -> !x.isDeleted())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		p.setName(req.name().trim());
		p.setDescription(req.description() == null ? null : req.description().trim());
		p.setPrice(req.price());
		p.setQuantity(req.quantity());

		return new ProductDtos.Response(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(),
				p.isAvailable());
	}

	@Transactional
	public void softDelete(Long id) {
		Product p = repo.findById(id).filter(x -> !x.isDeleted())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		p.setDeleted(true);
	}

}
