package org.test.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.test.app.entities.Product;

/**
 * Product Repository with JPA Repo
 *
 * @author Anwar
 * @since 1.0.0
 */


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	boolean existsByNameIgnoreCaseAndDeletedFalse(String name);
}
