package com.project.lastmiledelivery.repositories;

import com.project.lastmiledelivery.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Tìm role theo tên
    Role findByRoleName(String roleName);
}
