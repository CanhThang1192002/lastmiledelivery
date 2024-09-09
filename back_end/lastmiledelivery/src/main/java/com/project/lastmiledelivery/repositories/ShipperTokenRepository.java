package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Shipper;
import com.project.lastmiledelivery.models.ShipperToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipperTokenRepository extends JpaRepository<ShipperToken, Integer>{
    // tim tat ca cac token cua shipper
    List<ShipperToken> findByShipper(Shipper shipper);

    // tim shipperToken theo refreshToken
    Optional<ShipperToken> findByRefreshToken(String refreshToken);
}
