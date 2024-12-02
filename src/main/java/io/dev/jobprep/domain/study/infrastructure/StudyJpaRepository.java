package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.domain.entity.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyJpaRepository extends JpaRepository<Study, Long> {

    @Query("select std from Study std where std.id = :id")
    Optional<Study> findById(Long id);

    @Query("select std from Study std where std.deletedAt = null and std.status in ('RECRUITING')")
    List<Study> findRecruitingStudy();

    @Query("select std from Study std where std.deletedAt = null")
    List<Study> findNonDeletedAllStudy();

    @Query("select std from Study std where std.userId = :creatorId")
    Optional<Study> findStudyByCreatorId(Long creatorId);

    @Query("""
        select std from Study std join Users u on std.id = u.studyId
        where u.id = :userId and std.status in ('IN_PROGRESS', 'RECRUITMENT_CLOSED')
        and std.deletedAt = null
    """)
    Optional<Study> findGatheredStudyByUserId(Long userId);

    @Query("select distinct u.id from Users u where u.studyId = :studyId")
    Integer getAmountOfGatheredUser(Long studyId);

}
