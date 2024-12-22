package io.dev.jobprep.domain.experience_master_cl.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.dev.jobprep.domain.experience_master_cl.domain.QExpMasterCl.expMasterCl;

@Repository
@RequiredArgsConstructor
public class ExpMasterClRepositoryCustomImpl implements ExpMasterClRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ExpMasterCl> findByConditionWithPagination(Long userId, Long cursorId, int pageSize) {
        return jpaQueryFactory.selectFrom(expMasterCl)
                .where (
                        userIdEq(userId),
                        cursorIdCondition(cursorId),
                        activeTrue()
                )
                .orderBy(expMasterCl.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return expMasterCl.creator.id.eq(userId);
    }

    private BooleanExpression activeTrue() {
        return expMasterCl.active.eq(true);
    }

    private BooleanExpression cursorIdCondition(Long cursorId) {
        return cursorId != null ? expMasterCl.id.lt(cursorId) : null;
    }
}
