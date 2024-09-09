package com.project.lastmiledelivery.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shippertokens")
public class ShipperToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    @Column(name = "expires_date", nullable = false)
    private LocalDateTime expiresDate;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    @Column(name = "refresh_token", length = 255, nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "refresh_expires_date", nullable = false)
    private LocalDateTime refreshExpiresDate;

    @ManyToOne
    @JoinColumn(name = "shipper_id", referencedColumnName = "shipper_id", nullable = false)
    private Shipper shipper;
}
