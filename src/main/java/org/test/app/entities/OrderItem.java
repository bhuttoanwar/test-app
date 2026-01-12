package org.test.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Order Items Table Entity Mapping
 *
 * @author Anwar
 * @since 1.0.0
 */


@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discountApplied;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;
}
