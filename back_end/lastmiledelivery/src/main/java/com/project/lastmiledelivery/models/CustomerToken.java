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
@Table(name = "customertokens")
public class CustomerToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @Column(name = "token_type",length = 50, nullable = false)
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
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private Customer customer;
}
