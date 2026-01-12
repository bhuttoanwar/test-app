package org.test.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.app.entities.ConfigProp;

/**
 * Config Prop Repository with JPA Repo
 *
 * @author Anwar
 * @since 1.0.0
 */

public interface ConfigPropRepository extends JpaRepository<ConfigProp, Long> {

	Optional<ConfigProp> findByPropKeyAndActiveTrue(String propKey);
}
