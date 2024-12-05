package io.dev.jobprep.domain.applicationstatus.application;

import static io.dev.jobprep.exception.code.ErrorCode403.APPLICATION_STATUS_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.APPLICATION_STATUS_NOT_FOUND;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import io.dev.jobprep.domain.applicationstatus.exception.ApplicationStatusException;
import io.dev.jobprep.domain.applicationstatus.infrastructure.ApplicationStatusJpaRepository;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusCreateRequest;
import io.dev.jobprep.domain.applicationstatus.presentation.dto.req.ApplicationStatusUpdateRequest;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationStatusService {

    private final ApplicationStatusJpaRepository applicationStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(Long userId, ApplicationStatusCreateRequest req) {

        // TODO: 유저 토큰 검증
        User user = getUser(userId);

        ApplicationStatus applicationStatus = req.toEntity(user);
        save(applicationStatus);

        return applicationStatus.getId();
    }

    @Transactional
    public Long delete(Long userId, Long id) {

        // TODO: 유저 토큰 검증
        getUser(userId);

        validateCreator(userId, id);
        delete(id);
        return id;
    }

    public ApplicationStatus get(Long userId, Long id) {

        // TODO: 유저 토큰 검증
        getUser(userId);

        validateCreator(userId, id);
        return getApplicationStatus(id);
    }

    public List<ApplicationStatus> getAll(Long userId) {

        // TODO: 유저 토큰 검증
        getUser(userId);

        return applicationStatusRepository.findMyAppStatusAll(userId);
    }

    public void modify(Long userId, Long id, String field, ApplicationStatusUpdateRequest req) {

        // TODO: 유저 토큰 검증
        getUser(userId);

        ApplicationStatus status = getApplicationStatus(id);
        status.modify(field, req.getNewVal());
    }

    private void validateCreator(Long userId, Long id) {
        ApplicationStatus status = getApplicationStatus(id);
        if (!status.getCreator().getId().equals(userId)) {
            throw new ApplicationStatusException(APPLICATION_STATUS_FORBIDDEN_OPERATION);
        }
    }

    private void save(ApplicationStatus applicationStatus) {
        applicationStatusRepository.save(applicationStatus);
    }

    private User getUser(Long userId) {
        return userRepository.findUserById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private void delete(Long id) {
        applicationStatusRepository.deleteById(id);
    }

    private ApplicationStatus getApplicationStatus(Long id) {
        return applicationStatusRepository.findById(id)
            .orElseThrow(() -> new ApplicationStatusException(APPLICATION_STATUS_NOT_FOUND));
    }

}
