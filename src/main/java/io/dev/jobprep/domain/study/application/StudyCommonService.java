package io.dev.jobprep.domain.study.application;

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

    private final UserStudyJpaRepository userStudyRepository;

    public boolean isParticipator(Long studyId, Long userId) {
        return userStudyRepository.findByUserIdAndStudyId(studyId, userId).isPresent();
    }

}
