package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.CustomerToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerTokenRepository extends JpaRepository<CustomerToken, Integer> {
    // tim tat ca cac token cua customer
    List<CustomerToken> findByCustomer(Customer customer);

    // tim CustomerToken theo refreshToken
    Optional<CustomerToken> findByRefreshToken(String refreshToken);
}
