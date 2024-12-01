package io.dev.jobprep.users.infrastructure;

import io.dev.jobprep.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Boolean existsByEmail(String email);
}
