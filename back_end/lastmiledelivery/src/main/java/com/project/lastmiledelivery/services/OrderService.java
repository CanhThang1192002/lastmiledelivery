package com.project.lastmiledelivery.services;

import com.project.lastmiledelivery.dtos.OrderDto;
import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.models.Order;
import com.project.lastmiledelivery.models.OrderDetail;
import com.project.lastmiledelivery.repositories.CustomerRepository;
import com.project.lastmiledelivery.repositories.OrderDetailRepository;
import com.project.lastmiledelivery.repositories.OrderRepository;
import com.project.lastmiledelivery.utils.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    public Order createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findByCustomerId(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Order order = Order.builder()
                .customer(customer)
                .recipient(orderDto.getRecipient())
                .recipientPhone(orderDto.getRecipientPhone())
                .recipientAddress(orderDto.getRecipientAddress())
                .status(Enums.OrderStatus.PENDING)
                .isDelete(false)
                .build();
        order.onCreate();
        order = orderRepository.save(order);

        OrderDetail orderItem = OrderDetail.builder()
                .order(order)
                .productName(orderDto.getProductName())
                .mass(orderDto.getMass())
                .size(orderDto.getSize())
                .cod(orderDto.getCod())
                .shippingFee(orderDto.getShippingFee())
                .isDelete(false)
                .build();
        orderDetailRepository.save(orderItem);

        return order;
    }

    public Order updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setRecipient(orderDto.getRecipient());
        order.setRecipientPhone(orderDto.getRecipientPhone());
        order.setRecipientAddress(orderDto.getRecipientAddress());
        order.setStatus(orderDto.getStatus());
        order.onUpdate();
        order = orderRepository.save(order);

        OrderDetail orderItem = orderDetailRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        orderItem.setProductName(orderDto.getProductName());
        orderItem.setMass(orderDto.getMass());
        orderItem.setSize(orderDto.getSize());
        orderItem.setCod(orderDto.getCod());
        orderItem.setShippingFee(orderDto.getShippingFee());
        orderDetailRepository.save(orderItem);

        return order;
    }

    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setIsDelete(true);
        orderRepository.save(order);

        OrderDetail orderItem = orderDetailRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        orderItem.setIsDelete(true);
        orderDetailRepository.save(orderItem);
    }

    public Order getOrder(Integer orderId) {
        Order order =  orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getIsDelete()) {
            throw new RuntimeException("Order is deleted");
        }
        return order;
    }

    public OrderDetail getOrderDetail(Order order) {
        OrderDetail orderDetail =  orderDetailRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        if(orderDetail.getIsDelete()) {
            throw new RuntimeException("Order item is deleted");
        }
        return orderDetail;
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByIsDeleteFalse(pageable);
    }

    public Page<Order> getOrdersByCustomerId(Integer customerId, Pageable pageable) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return orderRepository.findAllByCustomerAndIsDeleteFalse(customer, pageable);
    }
}
