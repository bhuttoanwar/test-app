package org.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main entry point for the application.
 *
 * <p>Execution starts from the {@link #main(String[])} method.</p>
 *
 * @author Anwar
 * @since 1.0.0
 */
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
