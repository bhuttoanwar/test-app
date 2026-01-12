package org.test.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Config Prop Table Entity Mapping
 *
 * @author Anwar
 * @since 1.0.0
 */


@Entity
@Table(name = "config_prop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigProp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "prop_key", unique = true, nullable = false)
	private String propKey;

	@Column(name = "prop_value", nullable = false)
	private String propValue;

	@Column(name = "is_active", nullable = false)
	private boolean active;

}
