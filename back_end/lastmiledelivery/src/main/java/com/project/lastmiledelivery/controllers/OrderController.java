package com.project.lastmiledelivery.controllers;

import com.project.lastmiledelivery.dtos.OrderDto;
import com.project.lastmiledelivery.models.Order;
import com.project.lastmiledelivery.models.OrderDetail;
import com.project.lastmiledelivery.responses.BaseResponse;
import com.project.lastmiledelivery.responses.OrderResponse;
import com.project.lastmiledelivery.services.OrderService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createOrder(
            @Valid @RequestBody OrderDto orderDto
    ) {
        try {
            orderService.createOrder(orderDto);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Order created successfully")
                    .status(200)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateOrder(
            @Valid @RequestBody OrderDto orderDto
    ) {
        try {
            orderService.updateOrder(orderDto);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Order updated successfully")
                    .status(200)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteOrder(
            @Valid @RequestBody Integer orderId
    ) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Order deleted successfully")
                    .status(200)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @PathVariable Integer orderId
    ) {
        try {
            List<OrderResponse> orderBaseResponses = new ArrayList<>();
            Order order = orderService.getOrder(orderId);
            OrderDetail orderDetail = orderService.getOrderDetail(order);
            OrderResponse orderBaseResponse = new OrderResponse();
            orderBaseResponse.mapperOrderResponse(order, orderDetail);
            orderBaseResponses.add(orderBaseResponse);
            Pageable pageable = PageRequest.of(0, 1);
            Page<OrderResponse> ordersPage = new PageImpl<>(orderBaseResponses, pageable, orderBaseResponses.size());

            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Order retrieved successfully")
                    .data(ordersPage)
                    .status(200)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<BaseResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @PathVariable Integer customerId
    ) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<Order> orders = orderService.getOrdersByCustomerId(customerId, pageable);
            List<OrderResponse> orderResponses = orders.getContent().stream().map(order -> {
                OrderDetail orderDetail = orderService.getOrderDetail(order);
                OrderResponse orderBaseResponse = new OrderResponse();
                orderBaseResponse.mapperOrderResponse(order, orderDetail);
                return orderBaseResponse;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Orders retrieved successfully")
                    .data(new PageImpl<>(orderResponses, pageable, orders.getTotalElements()))
                    .status(200)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<Order> orders = orderService.getAllOrders(pageable);
            List<OrderResponse> orderResponses = orders.getContent().stream().map(order -> {
                OrderDetail orderDetail = orderService.getOrderDetail(order);
                OrderResponse orderBaseResponse = new OrderResponse();
                orderBaseResponse.mapperOrderResponse(order, orderDetail);
                return orderBaseResponse;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Orders retrieved successfully")
                    .data(new PageImpl<>(orderResponses, pageable, orders.getTotalElements()))
                    .status(200)
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }
}
