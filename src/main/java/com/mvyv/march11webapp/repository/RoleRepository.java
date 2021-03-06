package com.mvyv.march11webapp.repository;

import com.mvyv.march11webapp.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByRole(String role);
}
