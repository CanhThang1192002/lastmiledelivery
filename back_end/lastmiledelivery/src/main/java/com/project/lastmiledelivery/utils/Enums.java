package com.project.lastmiledelivery.utils;

public class Enums {
    public enum Role {
        ADMIN,
        CUSTOMER,
        SHIPPER,
    }

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELED,
        RETURNED
    }

    public enum ShipperStatus {
        ONLINE,
        OFFLINE
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    public enum AssignmentStatus {
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum DeliveryAssignmentStatus {
        SHIPPED,
        DELIVERED,
        RETURNED
    }
}
