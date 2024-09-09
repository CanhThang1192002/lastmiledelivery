package com.project.lastmiledelivery.controllers;

import com.project.lastmiledelivery.dtos.LoginDto;
import com.project.lastmiledelivery.dtos.RegisterCustomerDto;
import com.project.lastmiledelivery.dtos.RegisterShipperDto;
import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.Shipper;
import com.project.lastmiledelivery.responses.BaseResponse;
import com.project.lastmiledelivery.responses.CustomerResponse;
import com.project.lastmiledelivery.responses.LoginResponse;
import com.project.lastmiledelivery.responses.ShipperResponse;
import com.project.lastmiledelivery.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/customer/register")
    public ResponseEntity<BaseResponse> createCustomer(
            @Valid @RequestBody RegisterCustomerDto userDto
            ) {
        try {
            userService.RegisterCustomer(userDto);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Register successfully")
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @PostMapping("/shipper/register")
    public ResponseEntity<BaseResponse> createShipper(
            @Valid @RequestBody RegisterShipperDto userDto
            ) {
        try {
            userService.RegisterShipper(userDto);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Register successfully")
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @PostMapping("/customer/login")
    public ResponseEntity<BaseResponse> loginCustomer(
            @Valid @RequestBody LoginDto loginDto
            ) {
        try {
            LoginResponse loginResponse = userService.loginCustomer(loginDto.getPhoneNumber(), loginDto.getPassword());
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Login successfully")
                    .data(loginResponse)
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(e.getMessage())
                            .status(400)
                            .build());
        }
    }

    @PostMapping("/shipper/login")
    public ResponseEntity<BaseResponse> loginShipper(
            @Valid @RequestBody LoginDto loginDto
    ) {
        try {
            LoginResponse loginResponse = userService.loginShipper(loginDto.getPhoneNumber(), loginDto.getPassword());
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Login successfully")
                    .data(loginResponse)
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<BaseResponse> loginAdmin(
            @Valid @RequestBody LoginDto loginDto
    ) {
        try {
            LoginResponse loginResponse = userService.loginAdmin(loginDto.getPhoneNumber(), loginDto.getPassword());
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Login successfully")
                    .data(loginResponse)
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @GetMapping("/customer/all")
    public ResponseEntity<BaseResponse> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try{
            Pageable pageable = PageRequest.of(page, limit);
            Page<Customer> customers = userService.getAllCustomer(pageable);
            List<CustomerResponse> customerResponses = customers.getContent().stream().map(customer -> {
                CustomerResponse customerResponse = new CustomerResponse();
                customerResponse.mapperCustomerResponse(customer);
                return customerResponse;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Get all customers successfully")
                    .data(new PageImpl<>(customerResponses, pageable, customers.getTotalElements()))
                    .status(200)
                    .build());
        }
        catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.builder()
                            .message(e.getMessage())
                            .status(401)
                            .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @GetMapping("/shipper/all")
    public ResponseEntity<BaseResponse> getAllShippers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try{
            Pageable pageable = PageRequest.of(page, limit);
            Page<Shipper> shippers = userService.getAllShipper(pageable);
            List<ShipperResponse> shipperResponses = shippers.getContent().stream().map(shipper -> {
                ShipperResponse shipperResponse = new ShipperResponse();
                shipperResponse.mapperShipperResponse(shipper);
                return shipperResponse;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Get all shippers successfully")
                    .data(new PageImpl<>(shipperResponses, pageable, shippers.getTotalElements()))
                    .status(200)
                    .build());
        }
        catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.builder()
                            .message(e.getMessage())
                            .status(401)
                            .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }
}
