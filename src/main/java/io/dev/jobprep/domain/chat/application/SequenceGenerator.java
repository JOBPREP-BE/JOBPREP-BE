package io.dev.jobprep.domain.chat.application;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import io.dev.jobprep.domain.chat.domain.entity.document.SequenceId;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceGenerator {

    private final MongoOperations mongoOperations;

    public Long getNextSequence(String defaultId) {
        SequenceId sequenceId = mongoOperations.findAndModify(
            queryForDefaultId(defaultId),
            new Update().inc("seq", 1),
            options().returnNew(true).upsert(true),
            SequenceId.class
        );
        return Objects.isNull(sequenceId) ? 1 : sequenceId.getSeq();
    }

    private Query queryForDefaultId(String defaultId) {
        Query query = new Query();
        query.addCriteria(verifyDefaultId(defaultId));
        return query;
    }

    private Criteria verifyDefaultId(String defaultId) {
        return Criteria.where("_id").is(defaultId);
    }

}
