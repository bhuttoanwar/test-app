package org.test.app.service;

import org.springframework.stereotype.Service;
import org.test.app.entities.ConfigProp;
import org.test.app.repo.ConfigPropRepository;

/**
 * Config Property Service
 *
 * @author Anwar
 * @since 1.0.0
 */

@Service
public class ConfigPropertyQueryService {

	private final ConfigPropRepository repo;

	public ConfigPropertyQueryService(ConfigPropRepository repo) {
		this.repo = repo;
	}

	public String getJwtSecret() {
		return repo.findByPropKeyAndActiveTrue("JWT_SECRET").map(ConfigProp::getPropValue)
				.orElseThrow(() -> new IllegalStateException("JWT_SECRET missing"));
	}

	public long getJwtTtlSeconds() {
		return Long.parseLong(repo.findByPropKeyAndActiveTrue("JWT_TTL_SECONDS").map(ConfigProp::getPropValue)
				.orElseThrow(() -> new IllegalStateException("JWT_TTL_SECONDS missing")));
	}
}
