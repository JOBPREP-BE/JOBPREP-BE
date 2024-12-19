package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode403.STUDY_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.infrastructure.StudyScheduleJpaRepository;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyScheduleCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateRequest;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StudyScheduleService {

    private static final int WEEK = 7;

    private final UserRepository userRepository;
    private final StudyJpaRepository studyRepository;
    private final StudyScheduleJpaRepository studyScheduleRepository;
    private final StudyCommonService studyCommonService;

    @Transactional
    public Long create(Long id, StudyScheduleCreateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(id);

        Study study = req.getStudy();
        validateCreator(study, id);

        Long scheduleId = createForcedSchedule(req);
        createInitSchedule(study, req);

        return scheduleId;
    }

    @Transactional
    public void modify(Long id, Long studyId, StudyUpdateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(id);

        StudySchedule studySchedule = getStudySchedule(studyId, req.getWeekNumber());
        Study study = studySchedule.getStudy();

        validateCreatorOrParticipator(study, id);
        studySchedule.modify(req.getStartDate());
    }

    public List<StudySchedule> getAll(Long studyId) {
        return studyScheduleRepository.findAllById(studyId);
    }

    private Study getStudy(Long studyId) {
        return studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }

    private void validateCreatorOrParticipator(Study study, Long userId) {
        if (!study.isCreator(userId) && !studyCommonService.isParticipator(study.getId(), userId)) {
            throw new StudyException(STUDY_FORBIDDEN_OPERATION);
        }
    }

    private void validateCreator(Study study, Long userId) {
        if (!study.isCreator(userId)) {
            throw new StudyException(STUDY_FORBIDDEN_OPERATION);
        }
    }

    public StudySchedule getStudySchedule(Long studyId, int weekNum) {
        return studyScheduleRepository.findSpecificScheduleByStudyIdAndWeekNum(studyId, weekNum);
    }

    private Long createForcedSchedule(StudyScheduleCreateRequest req) {

        StudySchedule forcedSchedule = req.toEntity(req.getStartDate());
        studyScheduleRepository.save(forcedSchedule);
        return forcedSchedule.getId();
    }

    private void createInitSchedule(Study study, StudyScheduleCreateRequest req) {

        for (int i = 2; i <= study.getDuration_weeks(); i++) {
            StudySchedule emptySchedule = req.toInitEntity(
                study, calcuateNextStartDate(req.getStartDate(), i - 1), i
            );
            studyScheduleRepository.save(emptySchedule);
        }
    }

    private User getUser(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private LocalDateTime calcuateNextStartDate(LocalDateTime startDate, int weekNum) {
        return startDate.plusDays((long) weekNum * WEEK);
    }

}
