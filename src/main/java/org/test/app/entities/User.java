package org.test.app.entities;

import org.test.app.interfaces.UserRole;

import jakarta.persistence.*;
import lombok.*;

/**
 * Users Table Entity Mapping
 *
 * @author Anwar
 * @since 1.0.0
 */


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 120)
	private String username;

	@Column(nullable = false)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserRole role;

	@Column(name = "is_active",nullable = false)
	private boolean isActive;
}
