package com.project.lastmiledelivery.services;

import com.project.lastmiledelivery.components.JwtTokenUtils;
import com.project.lastmiledelivery.dtos.RegisterCustomerDto;
import com.project.lastmiledelivery.dtos.RegisterShipperDto;
import com.project.lastmiledelivery.exceptions.DataNotFoundException;
import com.project.lastmiledelivery.models.*;
import com.project.lastmiledelivery.repositories.*;
import com.project.lastmiledelivery.responses.LoginResponse;
import com.project.lastmiledelivery.utils.Enums;
import com.project.lastmiledelivery.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final ShipperRepository shipperRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public Customer RegisterCustomer(RegisterCustomerDto userDto){
        String phoneNumber = userDto.getPhoneNumber();
        if(customerRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if(shipperRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number registered with the driver");
        }
        // Tạo mới customer
        Customer customer = Customer.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .role(roleRepository.findByRoleName(Enums.Role.CUSTOMER.name()))
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .gender(userDto.getGender())
                .dateOfBirth(userDto.getDateOfBirth())
                .address(userDto.getAddress())
                .googleAccountId(userDto.getGoogleAccountId())
                .isActive(true)
                .isDelete(false)
                .password("")
                .build();
        customer.onCreate();
        // Mã hóa mật khẩu
        if (userDto.getGoogleAccountId() == 0) {
            String password = userDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            customer.setPassword(encodedPassword);
        }
        return customerRepository.save(customer);
    }

    public Shipper RegisterShipper(RegisterShipperDto userDto) {
        String phoneNumber = userDto.getPhoneNumber();
        if(shipperRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if(customerRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number registered with the customer");
        }
        // Tạo mới shipper
        Shipper shipper = Shipper.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .role(roleRepository.findByRoleName(Enums.Role.SHIPPER.name()))
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .gender(userDto.getGender())
                .dateOfBirth(userDto.getDateOfBirth())
                .address(userDto.getAddress())
                .licenseNumber(userDto.getLicenseNumber())
                .vehicleType(userDto.getVehicleType())
                .status(Enums.ShipperStatus.OFFLINE)
                .isActive(true)
                .isDelete(false)
                .build();
        shipper.onCreate();
        // Mã hóa mật khẩu
        String password = userDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        shipper.setPassword(encodedPassword);
        return shipperRepository.save(shipper);
    }

    public LoginResponse loginCustomer(String phoneNumber, String password) throws Exception {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        if(customer.getRole().getRoleName().equals(Enums.Role.ADMIN.name())){
            throw new DataNotFoundException(MessageKeys.ROLE_DOES_NOT_EXISTS);
        }
        if(customer.getIsDelete()){
            throw new DataNotFoundException(MessageKeys.USER_NOT_FOUND);
        }
        if(customer.getGoogleAccountId() == 0 && !passwordEncoder.matches(password, customer.getPassword())){
            throw new DataNotFoundException(MessageKeys.PASSWORD_NOT_MATCH);
        }
        if(!customer.getIsActive()){
            throw new DataNotFoundException(MessageKeys.USER_NOT_ACTIVE);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
               phoneNumber, password, customer.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtils.generateTokenCustomer(customer);
        CustomerToken customerToken = tokenService.addCustomerToken(customer, token);
        return LoginResponse.builder()
                .fullName(customer.getFullName())
                .phoneNumber(customer.getPhoneNumber())
                .customerId(customer.getCustomerId())
                .role(customer.getRole().getRoleName())
                .token(token)
                .refreshToken(customerToken.getRefreshToken())
                .build();
    }

    public LoginResponse loginShipper(String phoneNumber, String password) throws Exception {
        Shipper shipper = shipperRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        if(shipper.getIsDelete()){
            throw new DataNotFoundException(MessageKeys.USER_NOT_FOUND);
        }
        if(!passwordEncoder.matches(password, shipper.getPassword())){
            throw new DataNotFoundException(MessageKeys.PASSWORD_NOT_MATCH);
        }
        if(!shipper.getIsActive()){
            throw new DataNotFoundException(MessageKeys.USER_NOT_ACTIVE);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, shipper.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenUtils.generateTokenShipper(shipper);
        ShipperToken shipperToken = tokenService.addShipperToken(shipper, token);
        return LoginResponse.builder()
                .fullName(shipper.getFullName())
                .phoneNumber(shipper.getPhoneNumber())
                .customerId(shipper.getShipperId())
                .role(shipper.getRole().getRoleName())
                .token(token)
                .refreshToken(shipperToken.getRefreshToken())
                .build();
    }

    public LoginResponse loginAdmin(String phoneNumber, String password) throws Exception {
        Customer admin = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        if(admin.getRole().getRoleName().equals(Enums.Role.ADMIN.name())){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    phoneNumber, password, admin.getAuthorities());
            authenticationManager.authenticate(authenticationToken);
            String token = jwtTokenUtils.generateTokenCustomer(admin);
            CustomerToken customerToken = tokenService.addCustomerToken(admin, token);
            return LoginResponse.builder()
                    .fullName(admin.getFullName())
                    .phoneNumber(admin.getPhoneNumber())
                    .customerId(admin.getCustomerId())
                    .role(admin.getRole().getRoleName())
                    .token(token)
                    .refreshToken(customerToken.getRefreshToken())
                    .build();
        }else{
            throw new DataNotFoundException(MessageKeys.ROLE_DOES_NOT_EXISTS);
        }
    }

    public Page<Customer> getAllCustomer(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Page<Shipper> getAllShipper(Pageable pageable) {
        return shipperRepository.findAll(pageable);
    }
}
