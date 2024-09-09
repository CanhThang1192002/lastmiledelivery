package com.project.lastmiledelivery.configurations;

import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.Shipper;
import com.project.lastmiledelivery.repositories.CustomerRepository;
import com.project.lastmiledelivery.repositories.ShipperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomerRepository customerRepository;
    private final ShipperRepository shipperRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return (phoneNumber) -> {
            // Tìm kiếm người dùng trong Customer repository trước
            Optional<Customer> customer = customerRepository.findByPhoneNumber(phoneNumber);
            if (customer.isPresent()) {
                return new org.springframework.security.core.userdetails.User(
                        customer.get().getPhoneNumber(),
                        customer.get().getPassword(),
                        customer.get().getAuthorities());
            }
            // Nếu không tìm thấy trong Customer, tìm kiếm trong Shipper repository
            Optional<Shipper> shipper = shipperRepository.findByPhoneNumber(phoneNumber);
            if (shipper.isPresent()) {
                return new org.springframework.security.core.userdetails.User(
                        shipper.get().getPhoneNumber(),
                        shipper.get().getPassword(),
                        shipper.get().getAuthorities());
            }
            // Nếu không tìm thấy trong cả hai, ném ra ngoại lệ
            throw new UsernameNotFoundException("Cannot find user with phone number = " + phoneNumber);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}

