package org.test.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.app.entities.User;
import org.test.app.interfaces.UserRole;

import java.util.Optional;

/**
 * User Repository with JPA Repo
 *
 * @author Anwar
 * @since 1.0.0
 */


public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	Optional<User> findByUsernameAndRole(String username, UserRole role);

}
