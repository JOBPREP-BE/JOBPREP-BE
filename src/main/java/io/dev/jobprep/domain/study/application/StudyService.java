package io.dev.jobprep.domain.study.application;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_CREATED_STUDY;
import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_GATHERED_STUDY;
import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_PASSED_DUE_DATE;
import static io.dev.jobprep.exception.code.ErrorCode400.DUPLICATE_STUDY_NAME;
import static io.dev.jobprep.exception.code.ErrorCode400.STUDY_GATHERED_USER_EXCEED;
import static io.dev.jobprep.exception.code.ErrorCode403.USER_PERMISSION_SUSPENDED;
import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

import io.dev.jobprep.domain.study.application.dto.res.StudyInfoDto;
import io.dev.jobprep.domain.study.application.dto.res.StudyWithStartDateDto;
import io.dev.jobprep.domain.study.domain.entity.Study;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.study.infrastructure.StudyJpaRepository;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyScheduleCreateRequest;
import io.dev.jobprep.domain.study.presentation.dto.req.StudyUpdateAdminRequest;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final int MAX_HEAD_COUNT = 3;
    private static final int INIT_WEEK_NUM = 1;
    private static final int MAX_WEEK_NUM = 3;

    private final UserRepository userRepository;
    private final StudyJpaRepository studyRepository;
    private final StudyScheduleService studyScheduleService;

    @Transactional
    public Long create(Long id, StudyCreateRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User creator = getUser(id);

        if (creator.validateStillPenalized()) {
            throw new UserException(USER_PERMISSION_SUSPENDED);
        }

        validateAlreadyCreated(id);
        validateAlreadyGathered(id);

        Study study = req.toEntity(creator);
        study.join(creator);

        try {
            studyRepository.save(study);
        } catch (ConstraintViolationException e) {
            throw new StudyException(DUPLICATE_STUDY_NAME);
        }

        StudyScheduleCreateRequest scheduleReq = req.from(study);
        studyScheduleService.create(id, scheduleReq);

        return study.getId();
    }

    @Transactional
    public Long join(Long id, Long studyId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(id);

        if (user.validateStillPenalized()) {
            throw new UserException(USER_PERMISSION_SUSPENDED);
        }
        validateAlreadyGathered(id);

        StudyWithStartDateDto studyWithDate = getStudyWithStartDate(studyId);
        Study study = studyWithDate.getStudy();

        if (isPassedDueDate(studyWithDate.getStartDate())) {
            // 모집 기간이 지났으면 모집 종료
            study.close();
            throw new StudyException(ALREADY_PASSED_DUE_DATE);
        }

        study.join(user);

        // 모집 인원이 다 찼으면 모집 종료
        validateShouldClose(study);

        return studyId;
    }

    public Optional<Study> getGatheredStudy(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        return studyRepository.findGatheredStudyByUserId(userId);
    }

    public List<StudyInfoDto> getRecruitingStudy(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        // TODO: User 엔티티 추가 시, 양뱡향 연관관계 매핑 후 수정
        List<Study> studies = studyRepository.findRecruitingStudy();
        return studies.stream().map(
            (study) -> StudyInfoDto.of(
                    getStudyWithStartDate(study.getId()),
                    study.getUserAmountOfGathered())
        ).toList();
    }

    @Transactional
    public void delete(Long userId, Long studyId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        Study study = getStudy(studyId);
        study.delete(user);
    }

    // TODO: 트랜잭션 쪼개기
    @Transactional
    public void deleteForInternal() {

        // TODO: 마감일이 지났는데, 모집인원이 다 차지 않은 스터디 조회
        List<Study> underStaffedStudy = studyRepository.findUnderstaffedStudy(MAX_HEAD_COUNT);
        underStaffedStudy.forEach(Study::deleteForInternal);

        // TODO: 3주차 진행이 완료된 스터디 조회
        List<Study> finishedStudy = studyRepository.findFinishedStudy(MAX_WEEK_NUM);
        finishedStudy.forEach(Study::deleteForInternal);
    }

    public List<Study> getAll(Long userId) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        return studyRepository.findNonDeletedAllStudy();
    }

    @Transactional
    public void update(Long userId, Long studyId, String field, StudyUpdateAdminRequest req) {

        // TODO: 유저 존재 여부 및 토큰 유효성 검사
        User user = getUser(userId);

        Study study = getStudy(studyId);
        study.modifyLink(user, field, req.getLink());
    }

    private void validateAlreadyCreated(Long creatorId) {
        if (studyRepository.findStudyByCreatorId(creatorId).isPresent()) {
            throw new StudyException(ALREADY_CREATED_STUDY);
        }
    }

    private void validateAlreadyGathered(Long userId) {
        if (studyRepository.findGatheredStudyByUserId(userId).isPresent()) {
            throw new StudyException(ALREADY_GATHERED_STUDY);
        }
    }

    private void validateShouldClose(Study study) {
        int amount = study.getUserAmountOfGathered();
        if (amount > MAX_HEAD_COUNT) {
            throw new StudyException(STUDY_GATHERED_USER_EXCEED);
        } else if (amount == MAX_HEAD_COUNT){
            study.close();
        }
    }

    private boolean isPassedDueDate(LocalDateTime startDate) {
        LocalDate today = LocalDate.now();
        return today.isAfter(startDate.toLocalDate());
    }

    private StudyWithStartDateDto getStudyWithStartDate(Long studyId) {
        return studyRepository.getStudyWithStartDate(studyId)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }

    private User getUser(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private Study getStudy(Long id) {
        return studyRepository.findById(id)
            .orElseThrow(() -> new StudyException(STUDY_NOT_FOUND));
    }
}
