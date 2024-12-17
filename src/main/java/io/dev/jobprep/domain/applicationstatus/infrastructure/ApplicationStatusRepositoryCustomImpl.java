package io.dev.jobprep.domain.applicationstatus.infrastructure;

import static io.dev.jobprep.domain.applicationstatus.domain.entity.QApplicationStatus.applicationStatus;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicationStatusRepositoryCustomImpl implements ApplicationStatusRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ApplicationStatus> findByConditionWithPagination(
        Long userId, Long cursorId, int pageSize
    ) {

        return jpaQueryFactory.selectFrom(applicationStatus)
            .where(
                userIdEq(userId),
                cursorIdCondition(cursorId)
            )
            .orderBy(applicationStatus.id.desc())
            .limit(pageSize + 1)
            .fetch();

    }

    private BooleanExpression userIdEq(Long userId) {
        return applicationStatus.creator.id.eq(userId);
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? applicationStatus.id.lt(cursorId) : null;
    }

}
