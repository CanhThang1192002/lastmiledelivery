package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.responses.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Kiểm tra sự tồn tại của số điện thoại
    boolean existsByPhoneNumber(String phoneNumber);

    // Tìm kiếm customer theo số điện thoại
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    // Tìm kiếm customer theo customerId
    Optional<Customer> findByCustomerId(Integer customerId);

    // lay tat ca customer
    Page<Customer> findAll(Pageable pageable);
}
