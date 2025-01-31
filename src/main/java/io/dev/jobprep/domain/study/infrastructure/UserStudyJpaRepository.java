package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.domain.entity.UserStudy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStudyJpaRepository extends JpaRepository<UserStudy, Long> {

    @Query("select us from UserStudy us where us.study.id = :studyId and us.user.id = :userId")
    Optional<UserStudy> findByUserIdAndStudyId(Long studyId, Long userId);
}
