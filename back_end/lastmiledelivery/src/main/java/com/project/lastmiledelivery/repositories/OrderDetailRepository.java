package com.project.lastmiledelivery.repositories;
import com.project.lastmiledelivery.models.Order;
import com.project.lastmiledelivery.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // tim kiem order detail theo order
    Optional<OrderDetail> findByOrder(Order order);
}
