package com.project.lastmiledelivery.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.lastmiledelivery.models.Shipper;
import com.project.lastmiledelivery.utils.Enums;

import java.util.Date;

public class ShipperResponse {
    @JsonProperty("shipper_id")
    private Integer shipperId;

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

    @JsonProperty("license_number")
    private String licenseNumber;

    @JsonProperty("vehicle_type")
    private String vehicleType;

    @JsonProperty("is_active")
    private Boolean isActive;

    public void mapperShipperResponse(Shipper shipper) {
        this.shipperId = shipper.getShipperId();
        this.createdAt = shipper.getCreatedAt();
        this.phoneNumber = shipper.getPhoneNumber();
        this.fullName = shipper.getFullName();
        this.email = shipper.getEmail();
        this.gender = shipper.getGender();
        this.dateOfBirth = shipper.getDateOfBirth();
        this.address = shipper.getAddress();
        this.licenseNumber = shipper.getLicenseNumber();
        this.vehicleType = shipper.getVehicleType();
        this.isActive = shipper.getIsActive();
    }
}
