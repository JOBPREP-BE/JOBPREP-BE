package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode400.IMPOSSIBLE_TO_MODIFY_DATE;
import static io.dev.jobprep.exception.code.ErrorCode400.IMPOSSIBLE_TO_MODIFY_FIRST_DATE;
import static io.dev.jobprep.exception.code.ErrorCode403.STUDY_FORBIDDEN_OPERATION;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyScheduleJpaRepository;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyScheduleCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateRequest;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import java.time.LocalDate;
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

    private static final int FIRST_WEEK = 1;
    private static final int WEEK = 7;

    private final UserCommonService userCommonService;
    private final StudyCommonService studyCommonService;
    private final StudyScheduleJpaRepository studyScheduleRepository;

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

        validateCreatorOrParticipator(studySchedule.getStudy(), id);
        validateDate(studyId, req);

        studySchedule.modify(req.getStartDate());
    }

    public List<StudySchedule> getAll(Long studyId) {
        return studyScheduleRepository.findAllById(studyId);
    }

    private Study getStudy(Long studyId) {
        return studyCommonService.getStudyWithId(studyId);
    }

    private void validateCreatorOrParticipator(Study study, Long userId) {
        if (!study.isCreator(userId) && !studyCommonService.isParticipator(study.getId(), userId)) {
            throw new StudyException(STUDY_FORBIDDEN_OPERATION);
        }
    }

    private void validateDate(Long studyId, StudyUpdateRequest req) {

        if (req.getWeekNumber() == FIRST_WEEK) {
            throw new StudyException(IMPOSSIBLE_TO_MODIFY_FIRST_DATE);
        } else {
            StudySchedule previous = getStudySchedule(studyId, req.getWeekNumber() - 1);
            LocalDate previousDate = previous.getStart_date().toLocalDate();
            LocalDate modifiedDate = req.getStartDate().toLocalDate();

            if (modifiedDate.isBefore(previousDate) || modifiedDate.isEqual(previousDate)) {
                throw new StudyException(IMPOSSIBLE_TO_MODIFY_DATE);
            }
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
        return userCommonService.getUserWithId(id);
    }

    private LocalDateTime calcuateNextStartDate(LocalDateTime startDate, int weekNum) {
        return startDate.plusDays((long) weekNum * WEEK);
    }

}
