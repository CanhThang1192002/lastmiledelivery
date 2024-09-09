package com.project.lastmiledelivery.services;

import com.project.lastmiledelivery.components.JwtTokenUtils;
import com.project.lastmiledelivery.exceptions.DataNotFoundException;
import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.CustomerToken;
import com.project.lastmiledelivery.models.Shipper;
import com.project.lastmiledelivery.models.ShipperToken;
import com.project.lastmiledelivery.repositories.CustomerTokenRepository;
import com.project.lastmiledelivery.repositories.ShipperTokenRepository;
import com.project.lastmiledelivery.responses.LoginResponse;
import com.project.lastmiledelivery.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {
    private final CustomerTokenRepository customerTokenRepository;
    private final ShipperTokenRepository shipperTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    private static final int MAX_CUSTOMER_TOKEN = 3;
    private static final int MAX_SHIPPER_TOKEN = 1;
    @Value("${jwt.expiration}")
    private int EXPIRATION;

    @Value("${jwt.refreshable-expiration}")
    private int REFRESHABLE_EXPIRATION;

    public CustomerToken addCustomerToken(Customer customer, String token) {
        List<CustomerToken> customerTokens = customerTokenRepository.findByCustomer(customer);
        if (customerTokens.size() >= MAX_CUSTOMER_TOKEN) customerTokenRepository.delete(customerTokens.get(0));
        CustomerToken newToken = CustomerToken.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresDate(LocalDateTime.now().plusSeconds(EXPIRATION * 1L))
                .revoked(false)
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpiresDate(LocalDateTime.now().plusSeconds(REFRESHABLE_EXPIRATION * 1L))
                .customer(customer)
                .build();
        return customerTokenRepository.save(newToken);
    }

    public ShipperToken addShipperToken(Shipper shipper, String token) {
        List<ShipperToken> shipperTokens = shipperTokenRepository.findByShipper(shipper);
        if (shipperTokens.size() >= MAX_SHIPPER_TOKEN) shipperTokenRepository.delete(shipperTokens.get(0));
        ShipperToken newToken = ShipperToken.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresDate(LocalDateTime.now().plusSeconds(EXPIRATION * 1L))
                .revoked(false)
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpiresDate(LocalDateTime.now().plusSeconds(REFRESHABLE_EXPIRATION * 1L))
                .shipper(shipper)
                .build();
        return shipperTokenRepository.save(newToken);
    }

    public LoginResponse refreshCustomerToken(String refreshToken) throws Exception {
        CustomerToken customerToken = customerTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.REFRESH_TOKEN_NOT_FOUND));
        if (customerToken.getRefreshExpiresDate().isBefore(LocalDateTime.now())) {
            customerToken.setRevoked(true);
            throw new DataNotFoundException(MessageKeys.TOKEN_EXPIRED);
        }
        String newToken = jwtTokenUtils.generateTokenCustomer(customerToken.getCustomer());
        customerToken.setToken(newToken);
        customerToken.setExpiresDate(LocalDateTime.now().plusSeconds(EXPIRATION * 1L));
        customerToken.setRefreshExpiresDate(LocalDateTime.now().plusSeconds(REFRESHABLE_EXPIRATION * 1L));
        customerToken =  customerTokenRepository.save(customerToken);
        return LoginResponse.builder()
                .fullName(customerToken.getCustomer().getFullName())
                .phoneNumber(customerToken.getCustomer().getPhoneNumber())
                .customerId(customerToken.getCustomer().getCustomerId())
                .role(customerToken.getCustomer().getRole().getRoleName())
                .token(customerToken.getToken())
                .refreshToken(customerToken.getRefreshToken())
                .build();
    }

    public LoginResponse refreshShipperToken(String refreshToken) throws Exception {
        ShipperToken shipperToken = shipperTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.REFRESH_TOKEN_NOT_FOUND));
        if (shipperToken.getRefreshExpiresDate().isBefore(LocalDateTime.now())) {
            shipperToken.setRevoked(true);
            throw new DataNotFoundException(MessageKeys.TOKEN_EXPIRED);
        }
        String newToken = jwtTokenUtils.generateTokenShipper(shipperToken.getShipper());
        shipperToken.setToken(newToken);
        shipperToken.setExpiresDate(LocalDateTime.now().plusSeconds(EXPIRATION * 1L));
        shipperToken.setRefreshExpiresDate(LocalDateTime.now().plusSeconds(REFRESHABLE_EXPIRATION * 1L));
        shipperToken = shipperTokenRepository.save(shipperToken);
        return LoginResponse.builder()
                .fullName(shipperToken.getShipper().getFullName())
                .phoneNumber(shipperToken.getShipper().getPhoneNumber())
                .customerId(shipperToken.getShipper().getShipperId())
                .role(shipperToken.getShipper().getRole().getRoleName())
                .token(shipperToken.getToken())
                .refreshToken(shipperToken.getRefreshToken())
                .build();
    }

}
