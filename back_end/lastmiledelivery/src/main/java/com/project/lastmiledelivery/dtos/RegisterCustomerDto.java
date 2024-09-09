package com.project.lastmiledelivery.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.lastmiledelivery.utils.Enums;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDto {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Full name cannot be blank")
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

    @JsonProperty("google_account_id")
    private int googleAccountId;
}
