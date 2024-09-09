package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Integer> {
}
