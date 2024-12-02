package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_CREATED_STUDY;
import static io.dev.jobprep.exception.code.ErrorCode400.STUDY_GATHERED_USER_EXCEED;
import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;

import io.dev.jobprep.domain.study.application.dto.res.StudyInfoDto;
import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.domain.entity.StudySchedule;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyScheduleCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateAdminRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StudyService {

    private final static int MAX_HEAD_COUNT = 3;
    private final static int INIT_WEEK_NUM = 1;

    private final StudyJpaRepository studyRepository;
    private final StudyScheduleService studyScheduleService;

    @Transactional
    public Long create(Long id, StudyCreateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        validateAlreadyCreated(id);

        // TODO: 현재 참여 중인 스터디가 있는지 유효성 검사

        Study study = req.toEntity(id);
        studyRepository.save(study);

        StudyScheduleCreateRequest scheduleReq = req.from(study.getId());
        studyScheduleService.create(id, scheduleReq);

        return study.getId();
    }

    public Long join(Long id, Long studyId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        Study study = validateAvailableJoin(studyId);


        // TODO: 해당 스터디가 모집 인원을 모두 채웠는지 유효성 검사
        // TODO: 해당 스터디가 모집 인원을 모두 채웠으면 '모집 마감' 변경
        validateMaxHeadCount(study);

        return studyId;
    }

    public Optional<Study> getGatheredStudy(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        // TODO: User 엔티티 추가 시, 양뱡향 연관관계 매핑 후 수정
        Optional<Study> study = studyRepository.findGatheredStudyByUserId(userId);
        if (study.isPresent()) {
            List<StudySchedule> schedules = studyScheduleService.getAll(study.get().getId());
        }

        return studyRepository.findGatheredStudyByUserId(userId);
    }

    public List<StudyInfoDto> getRecruitingStudy(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        // TODO: User 엔티티 추가 시, 양뱡향 연관관계 매핑 후 수정
        List<Study> studies = studyRepository.findRecruitingStudy();
        return studies.stream().map(
            (study) -> StudyInfoDto.of(
                    study,
                    studyScheduleService.getStudySchedule(study.getId(), INIT_WEEK_NUM),
                    studyRepository.getAmountOfGatheredUser(study.getId()))
        ).toList();
    }

    @Transactional
    public void delete(Long userId, Long studyId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        Study study = getStudy(studyId);
        study.delete(userId);
    }

    public List<Study> getAll(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        return studyRepository.findNonDeletedAllStudy();
    }

    public void update(Long userId, Long studyId, String field, StudyUpdateAdminRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사

        Study study = getStudy(studyId);
        study.modifyLink(userId, field, req.getLink());
    }

    private void validateAlreadyCreated(Long creatorId) {
        studyRepository.findStudyByCreatorId(creatorId)
            .orElseThrow(() -> new StudyException(ALREADY_CREATED_STUDY));
    }

    private Study validateAvailableJoin(Long studyId) {
        Study study = getStudy(studyId);
        // 해당 스터디가 '모집중'인지 유효성 검사
        study.validateAvailableJoin();
        return study;
    }

    private void validateMaxHeadCount(Study study) {
        int amount = studyRepository.getAmountOfGatheredUser(study.getId());
        if (amount >= MAX_HEAD_COUNT) {
            throw new StudyException(STUDY_GATHERED_USER_EXCEED);
        } else if (amount == MAX_HEAD_COUNT - 1){
            study.close();
        }
    }

    private Study getStudy(Long id) {
        return studyRepository.findById(id)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }
}
