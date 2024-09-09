package com.project.lastmiledelivery.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Order_Details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "mass", nullable = false)
    private BigDecimal mass; // Use BigDecimal for precision

    @Column(name = "size", nullable = false)
    private BigDecimal size; // Use BigDecimal for precision

    @Column(name = "cod", nullable = false, precision = 10, scale = 2)
    private BigDecimal cod; // Use BigDecimal for precision and scale

    @Column(name = "shipping_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingFee; // Use BigDecimal for precision and scale

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    // Lifecycle Callbacks
    @PrePersist
    @PreUpdate
    protected void onPersistAndUpdate() {
        // Add any additional logic here if needed
    }
}
