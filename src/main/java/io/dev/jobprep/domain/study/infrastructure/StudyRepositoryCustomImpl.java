package io.dev.jobprep.domain.study.infrastructure;

import static io.dev.jobprep.domain.study.domain.entity.QStudy.study;
import static io.dev.jobprep.domain.study.domain.entity.QStudySchedule.studySchedule;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dev.jobprep.domain.study.application.dto.res.StudyWithStartDateDto;
import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.enums.StudyStatus;
import java.util.List;
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

    @Override
    public List<Study> findNonDeletedStudyWithPagination(Long cursorId, int pageSize) {
        return jpaQueryFactory.selectFrom(study)
            .where(
                cursorIdCondition(cursorId),
                deletedAtEq()
            )
            .limit(pageSize + 1)
            .fetch();
    }

    @Override
    public List<Study> findRecruitingStudyWithPagination(int page, int pageSize, int pageGroupSize) {
        int offset = (page - 1) * pageSize;

        return jpaQueryFactory.selectFrom(study)
            .where(
                studyStatusEq(StudyStatus.RECRUITING),
                deletedAtEq()
            )
            .offset(offset)
            .limit((long) pageSize * pageGroupSize)
            .orderBy(study.id.desc())
            .fetch();
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? study.id.lt(cursorId) : null;
    }

    private BooleanExpression studyStatusEq(StudyStatus status) {
        return study.status.eq(status);
    }

    private BooleanExpression deletedAtEq() {
        return study.deletedAt.isNull();
    }
}
