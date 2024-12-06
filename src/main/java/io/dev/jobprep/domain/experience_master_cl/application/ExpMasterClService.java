package io.dev.jobprep.domain.experience_master_cl.application;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import io.dev.jobprep.domain.experience_master_cl.domain.enums.ExpAnalProcess;
import io.dev.jobprep.domain.experience_master_cl.domain.enums.MasterClProcess;
import io.dev.jobprep.domain.experience_master_cl.exception.ExpMasterClException;
import io.dev.jobprep.domain.experience_master_cl.infrastructure.ExpMasterClRepository;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.req.ExpMasterClPatchRequest;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.ExpMasterClIdResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindAllExpMasterClResponse;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.res.FindExpMasterClResponse;
import io.dev.jobprep.domain.job_interview.exception.JobInterviewException;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.exception.UserException;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.dev.jobprep.exception.code.ErrorCode400.INVALID_INPUT_VALUE;
import static io.dev.jobprep.exception.code.ErrorCode403.MASTER_CL_FORBIDDEN_OPERATION;
import static io.dev.jobprep.exception.code.ErrorCode404.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExpMasterClService {
    private final ExpMasterClRepository expMasterClRepository;
    private final UserRepository userRepository;

    @Transactional
    public ExpMasterClIdResponse save (Long userId) {
        User user = getUser(userId);

        ExpMasterCl expMasterCl = ExpMasterCl.builder()
                .material("")
                .emphasis("")
                .expAnalProcess(ExpAnalProcess.PREPARATION)
                .masterClProcess(MasterClProcess.PREPARATION)
                .expAnal("")
                .masterCl("")
                .creator(user)
                .active(true)
                .build();

        expMasterClRepository.save(expMasterCl);
        return ExpMasterClIdResponse.from(expMasterCl.getId());
    }

    @Transactional
    public FindExpMasterClResponse patch (Long id, Long userId, ExpMasterClPatchRequest request) {
        User user = getUser(userId);
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getId());

        expMasterCl.update(request);

        return FindExpMasterClResponse.toDto(expMasterCl);
    }

    @Transactional
    public void delete (Long id, Long userId) {
        User user = getUser(userId);
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getId());
        expMasterCl.disable();
    }

    public List<FindAllExpMasterClResponse> findAll (Long userId) {
        User user = getUser(userId);
        List<ExpMasterCl> expMasterClList = expMasterClRepository.findAllByActiveTrueAndCreatorId(user.getId());

        return expMasterClList.stream()
                .map(FindAllExpMasterClResponse::toDto)
                .collect(Collectors.toList());
    }

    public FindExpMasterClResponse find (Long id, Long userId) {
        User user = getUser(userId);
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getId());
        return FindExpMasterClResponse.toDto(expMasterCl);
    }

    private ExpMasterCl findById (Long id) {
        return expMasterClRepository.findById(id)
                .orElseThrow(() -> new ExpMasterClException(INVALID_INPUT_VALUE));
    }

    private User getUser(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private void validateUser(Long userId, Long jobInterviewId) {
        if (!Objects.equals(userId, jobInterviewId)) {
            throw new JobInterviewException(MASTER_CL_FORBIDDEN_OPERATION);
        }
    }
}
