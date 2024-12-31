package io.dev.jobprep.domain.job_interview.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.dev.jobprep.domain.job_interview.domain.QJobInterview.jobInterview;

@Repository
@RequiredArgsConstructor
public class JobInterviewRepositoryCustomImpl implements JobInterviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<JobInterview> findByConditionWithPagination(Long userId, Long cursorId, int pageSize) {
        return jpaQueryFactory.selectFrom(jobInterview)
                .where(
                        userIdEq(userId),
                        cursorIdCondition(cursorId)
                )
                .orderBy(jobInterview.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? jobInterview.id.lt(cursorId) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return jobInterview.creator.id.eq(userId);
    }

}
