package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Integer> {
    // tim kiem order theo id
    Optional<Order> findByOrderId(Integer orderId);

    // tim kiem order theo id cua customer
    Page<Order> findByCustomer(Customer customer, Pageable pageable);

    // lấy tất cả order chưa xóa
    Page<Order> findAllByIsDeleteFalse(Pageable pageable);

    // lấy tất cả order chưa xóa theo customer
    Page<Order> findAllByCustomerAndIsDeleteFalse(Customer customer, Pageable pageable);
}
