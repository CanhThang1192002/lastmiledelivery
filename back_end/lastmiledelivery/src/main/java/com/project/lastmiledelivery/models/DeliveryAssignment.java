package com.project.lastmiledelivery.models;

import com.project.lastmiledelivery.utils.Enums;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DeliveryAssignments")
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "shipper_id", referencedColumnName = "shipper_id", nullable = false)
    private Shipper shipper;

    @Column(name = "assigned_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date assignedAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'ASSIGNED'")
    private Enums.AssignmentStatus status = Enums.AssignmentStatus.ASSIGNED;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @PrePersist
    protected void onCreate() {
        assignedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        // Optionally handle updates if needed
    }
}
