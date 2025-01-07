package com.haot.user.infrastructure.repository;

import com.haot.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
}
