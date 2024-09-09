package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Integer> {
}
