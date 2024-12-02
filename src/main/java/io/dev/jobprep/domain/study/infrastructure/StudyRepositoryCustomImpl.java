package io.dev.jobprep.domain.study.infrastructure;

import static io.dev.jobprep.domain.study.domain.entity.QStudy.study;
import static io.dev.jobprep.domain.study.domain.entity.QStudySchedule.studySchedule;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dev.jobprep.domain.study.application.dto.res.StudyWithStartDateDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryCustomImpl implements StudyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StudyWithStartDateDto> getStudyWithStartDate(Long studyId) {
        Tuple result = jpaQueryFactory.select(study, studySchedule.start_date)
            .from(study)
            .innerJoin(studySchedule).on(study.id.eq(studySchedule.study.id))
            .where(studySchedule.week_number.eq(1), study.id.eq(studyId))
            .fetchOne();

        return Optional.ofNullable(
            StudyWithStartDateDto.of(
                result.get(study),
                result.get(studySchedule.start_date)
        ));
    }
}
