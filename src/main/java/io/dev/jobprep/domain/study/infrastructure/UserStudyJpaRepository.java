package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.domain.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStudyJpaRepository extends JpaRepository<UserStudy, Long> {


}
