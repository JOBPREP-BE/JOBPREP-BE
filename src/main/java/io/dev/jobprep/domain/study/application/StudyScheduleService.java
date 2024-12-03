package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;

import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.infrastructure.StudyScheduleJpaRepository;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyScheduleCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateRequest;
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

    private final StudyJpaRepository studyRepository;
    private final StudyScheduleJpaRepository studyScheduleRepository;

    public Long create(Long id, StudyScheduleCreateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        // TODO: 해당 유저가 스터디 생성자가 맞는지 검사

        Study study = getStudy(req.getStudyId());

        Long scheduleId = createForcedSchedule(study, req);
        createEmptySchedule(study, req);

        return scheduleId;
    }

    public void modify(Long id, Long studyId, StudyUpdateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        // TODO: 해당 유저가 스터디 참여자가 맞는지 검사

        StudySchedule studySchedule = getStudySchedule(studyId, req.getWeekNumber());
        studySchedule.modify(req.getStartDate());
    }

    public List<StudySchedule> getAll(Long studyId) {
        return studyScheduleRepository.findAllById(studyId);
    }

    private Study getStudy(Long studyId) {
        return studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }

    public StudySchedule getStudySchedule(Long studyId, int weekNum) {
        return studyScheduleRepository.findSpecificScheduleByStudyIdAndWeekNum(studyId, weekNum);
    }

    private Long createForcedSchedule(Study study, StudyScheduleCreateRequest req) {

        StudySchedule forcedSchedule = req.toEntity(study, req.getStartDate());
        studyScheduleRepository.save(forcedSchedule);
        return forcedSchedule.getId();
    }

    private void createEmptySchedule(Study study, StudyScheduleCreateRequest req) {

        for (int i = 1; i < study.getDuration_weeks(); i++) {
            StudySchedule emptySchedule = req.toInitEntity(study, i);
            studyScheduleRepository.save(emptySchedule);
        }
    }

}
