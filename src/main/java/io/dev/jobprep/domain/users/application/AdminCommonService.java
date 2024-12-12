package io.dev.jobprep.domain.users.application;

import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_USER;
import static io.dev.jobprep.exception.code.ErrorCode403.ADMIN_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.UserStudy;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.infrastructure.UserStudyJpaRepository;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.domain.UserRole;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.presentation.dto.req.UserPenalizeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminCommonService {

    private final UserCommonService userCommonService;
    private final StudyJpaRepository studyRepository;
    private final UserStudyJpaRepository userStudyRepository;

    @Transactional
    public Long penalize(Long userId, UserPenalizeRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User admin = getUser(userId);

        if (!validateisAdmin(userId)) {
            throw new UserException(ADMIN_FORBIDDEN_OPERATION);
        }

        User fugitive = getUser(req.getUserId());
        fugitive.penalize();

        Study study = getStudy(req.getName());
        UserStudy userStudy = validateGatheredUser(fugitive.getId(), study.getId());
        study.kickOut(userStudy);

        return study.getId();
    }

    private User getUser(Long userId) {
        return userCommonService.getUserWithId(userId);
    }

    private Study getStudy(String name) {
        return studyRepository.findStudyByName(name)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }

    private boolean validateisAdmin(Long userId) {
        return userCommonService.getUserWithId(userId).getUserRole().equals(UserRole.ADMIN);
    }

    private UserStudy validateGatheredUser(Long userId, Long studyId) {
        return userStudyRepository.findByUserIdAndStudyId(studyId, userId)
            .orElseThrow(() -> new StudyException(NON_GATHERED_USER));
    }

}
