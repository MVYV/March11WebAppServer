package com.mvyv.march11webapp.repository;

import com.mvyv.march11webapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findUserByEmail(@Param("email") String email);

  User findUserByUserName(@Param("userName") String userName);
}
