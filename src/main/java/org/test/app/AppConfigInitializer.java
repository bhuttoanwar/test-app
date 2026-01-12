package org.test.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.test.app.entities.ConfigProp;
import org.test.app.entities.User;
import org.test.app.interfaces.UserRole;
import org.test.app.repo.ConfigPropRepository;
import org.test.app.repo.UserRepository;
import org.test.app.util.AppUtil;

import jakarta.annotation.PostConstruct;

/**
 * App Configuration Initializer To Insert JWT Secret Key on App Start up
 *
 * @author Anwar
 * @since 1.0.0
 */

@Component
public class AppConfigInitializer {

	private static final String JWT_SECRET_KEY = "JWT_SECRET";
	private static final String JWT_TTL_KEY = "JWT_TTL_SECONDS";

	private final ConfigPropRepository repo;
	private final UserRepository userRepository;
	private final AppUtil appUtil;

	@Value("${default.user.id}")
	private String defaultUserID;

	@Value("${default.user.pwd}")
	private String defaultUserPwd;

	public AppConfigInitializer(ConfigPropRepository repo, UserRepository userRepository, AppUtil appUtil) {
		this.repo = repo;
		this.userRepository = userRepository;
		this.appUtil = appUtil;
	}

	@PostConstruct
	public void init() {
		initJwtSecret();
		initJwtTtl();
		initAdminUser();

	}

	private void initJwtSecret() {
		repo.findByPropKeyAndActiveTrue(JWT_SECRET_KEY).orElseGet(() -> {
			String secret = appUtil.generateSecureKey();
			ConfigProp prop = new ConfigProp();
			prop.setPropKey(JWT_SECRET_KEY);
			prop.setPropValue(secret);
			prop.setActive(true);
			return repo.save(prop);
		});
	}

	private void initJwtTtl() {
		repo.findByPropKeyAndActiveTrue(JWT_TTL_KEY).orElseGet(() -> {
			ConfigProp prop = new ConfigProp();
			prop.setPropKey(JWT_TTL_KEY);
			prop.setPropValue("3600"); // 1 hour default
			prop.setActive(true);
			return repo.save(prop);
		});
	}

	private void initAdminUser() {
		if (userRepository.count() == 0) {
			String pwdHash = appUtil.passwordEncoder().encode(defaultUserPwd);
			User admin = User.builder().username(defaultUserID).passwordHash(pwdHash).role(UserRole.ADMIN).isActive(true)
					.build();

			userRepository.save(admin);
		}
	}
}
