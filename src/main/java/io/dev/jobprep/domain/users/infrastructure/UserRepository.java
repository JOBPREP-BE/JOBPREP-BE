package io.dev.jobprep.domain.users.infrastructure;

import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.userRole = :role")
    Optional<User> findUserByRole(UserRole role);
}
