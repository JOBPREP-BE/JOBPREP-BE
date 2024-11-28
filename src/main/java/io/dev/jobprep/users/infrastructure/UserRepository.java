package io.dev.jobprep.users.infrastructure;

import io.dev.jobprep.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
