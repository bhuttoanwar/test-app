package org.test.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Products Table Entity Mapping
 *
 * @author Anwar
 * @since 1.0.0
 */


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(length = 2000)
	private String description;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private boolean deleted = false;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public boolean isAvailable() {
		return !deleted && quantity != null && quantity > 0;
	}
}
