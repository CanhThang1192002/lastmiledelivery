package com.project.lastmiledelivery.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.lastmiledelivery.utils.Enums;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("customer_id")
//    @NotBlank(message = "Customer ID is required")
    private Integer customerId;

    @JsonProperty("recipient")
//    @NotBlank(message = "Recipient is required")
    private String recipient;

    @JsonProperty("recipient_phone")
//    @NotBlank(message = "Recipient phone is required")
    private String recipientPhone;

    @JsonProperty("recipient_address")
//    @NotBlank(message = "Recipient address is required")
    private String recipientAddress;

    @JsonProperty("status")
//    @NotBlank(message = "Status is required")
    private Enums.OrderStatus status;

    @JsonProperty("product_name")
//    @NotBlank(message = "Product name is required")
    private String productName;

    @JsonProperty("mass")
//    @NotBlank(message = "Mass is required")
    private BigDecimal mass;

    @JsonProperty("size")
//    @NotBlank(message = "Size is required")
    private BigDecimal size;

    @JsonProperty("cod")
//    @NotBlank(message = "COD is required")
    private BigDecimal cod;

    @JsonProperty("shipping_fee")
    private BigDecimal shippingFee;
}
