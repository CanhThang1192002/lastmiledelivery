package com.project.lastmiledelivery.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.lastmiledelivery.models.Order;
import com.project.lastmiledelivery.models.OrderDetail;
import com.project.lastmiledelivery.utils.Enums;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("recipient")
    private String recipient;

    @JsonProperty("recipient_phone")
    private String recipientPhone;

    @JsonProperty("recipient_address")
    private String recipientAddress;

    @JsonProperty("status")
    private Enums.OrderStatus status;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("mass")
    private BigDecimal mass;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("cod")
    private BigDecimal cod;

    @JsonProperty("shipping_fee")
    private BigDecimal shippingFee;

    public void mapperOrderResponse(Order order, OrderDetail orderDetail){
        this.orderId = order.getOrderId();
        this.createdAt = order.getCreatedAt();
        this.recipient = order.getRecipient();
        this.recipientPhone = order.getRecipientPhone();
        this.recipientAddress = order.getRecipientAddress();
        this.status = order.getStatus();
        this.productName = orderDetail.getProductName();
        this.mass = orderDetail.getMass();
        this.size = orderDetail.getSize();
        this.cod = orderDetail.getCod();
        this.shippingFee = orderDetail.getShippingFee();
    }
}
