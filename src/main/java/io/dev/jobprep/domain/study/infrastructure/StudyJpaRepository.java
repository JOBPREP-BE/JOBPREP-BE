package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.domain.entity.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyJpaRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

    @Query("select std from Study std where std.id = :id")
    Optional<Study> findById(Long id);

    @Query("select std from Study std where std.deletedAt = null and std.status in ('RECRUITING')")
    List<Study> findRecruitingStudy();

    @Query("select std from Study std where std.deletedAt = null")
    List<Study> findNonDeletedAllStudy();

    @Query("select std from Study std where std.creator.id = :creatorId")
    Optional<Study> findStudyByCreatorId(Long creatorId);

    @Query("select std from Study std where std.name = :name")
    Optional<Study> findStudyByName(String name);

    @Query("""
        select std from Study std join UserStudy us on std.id = us.study.id
        where us.user.id = :userId and not std.status in ('FINISHED') and std.deletedAt = null
    """)
    Optional<Study> findGatheredStudyByUserId(Long userId);

    @Query(value = """
        select * from study as s left join user_study us on s.id = us.study_id
        where s.study_status = 'RECRUITING' group by s.id having count(us.user_id) < :headCount
    """, nativeQuery = true)
    List<Study> findUnderstaffedStudy(int headCount);

    @Query(value = """
        select * from study as s inner join study_schedule ss on s.id = ss.study_id
        where ss.week_number = :weekNum and ss.start_date = CURDATE() - INTERVAL 1 DAY;
    """, nativeQuery = true)
    List<Study> findFinishedStudy(int weekNum);

}
