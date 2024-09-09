package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipperRepository extends JpaRepository<Shipper, Integer> {
    // Kiểm tra sđt đã tồn tại chưa
    boolean existsByPhoneNumber(String phoneNumber);
    // Tìm shipper theo sđt
    Optional<Shipper> findByPhoneNumber(String phoneNumber);
}
