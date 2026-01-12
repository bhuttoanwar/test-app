package org.test.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.app.entities.Order;

/**
 * Orders Repository with JPA Repo
 *
 * @author Anwar
 * @since 1.0.0
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
}
