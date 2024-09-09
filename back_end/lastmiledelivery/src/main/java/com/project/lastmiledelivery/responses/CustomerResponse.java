package com.project.lastmiledelivery.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.lastmiledelivery.models.Customer;
import com.project.lastmiledelivery.utils.Enums;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private Enums.Gender gender;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("address")
    private String address;

    @JsonProperty("is_active")
    private Boolean isActive;

    public void mapperCustomerResponse(Customer customer) {
        this.createdAt = customer.getCreatedAt();
        this.phoneNumber = customer.getPhoneNumber();
        this.fullName = customer.getFullName();
        this.email = customer.getEmail();
        this.gender = customer.getGender();
        this.dateOfBirth = customer.getDateOfBirth();
        this.address = customer.getAddress();
        this.isActive = customer.getIsActive();
    }
}
