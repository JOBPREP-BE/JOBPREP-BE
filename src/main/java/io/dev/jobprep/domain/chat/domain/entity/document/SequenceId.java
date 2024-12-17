package io.dev.jobprep.domain.chat.domain.entity.document;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "counters")
public class SequenceId {

    @Id
    private final String id;

    private final Long seq;

    private SequenceId(String id, Long seq) {
        this.id = id;
        this.seq = seq;
    }

}
