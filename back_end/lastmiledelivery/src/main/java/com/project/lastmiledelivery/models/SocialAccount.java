package com.project.lastmiledelivery.models;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "social_accounts")
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 50, nullable = false)
    private String providerId;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

}

