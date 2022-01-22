package com.upperlink.ulidappportal.repository;

import com.upperlink.ulidappportal.domain.ERole;
import com.upperlink.ulidappportal.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

}
