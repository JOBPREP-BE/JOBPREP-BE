package io.dev.jobprep.domain.study.infrastructure;

import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyScheduleJpaRepository extends JpaRepository<StudySchedule, Long> {

    @Query("select sch from StudySchedule sch where sch.study.id = :studyId and sch.week_number = :weekNum")
    StudySchedule findSpecificScheduleByStudyIdAndWeekNum(Long studyId, int weekNum);

    @Query("select sch from StudySchedule sch where sch.study.id = :studyId")
    List<StudySchedule> findAllById(Long studyId);
}
