package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.infrastructure.UserStudyJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyCommonService {

    private final StudyJpaRepository studyRepository;
    private final UserStudyJpaRepository userStudyRepository;

    public boolean isParticipator(Long studyId, Long userId) {
        return userStudyRepository.findByUserIdAndStudyId(studyId, userId).isPresent();
    }

    public Study getStudyWithId(Long studyId) {
        return studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }

}
