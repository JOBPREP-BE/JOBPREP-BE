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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExpMasterClService {
    private final ExpMasterClRepository expMasterClRepository;

    @Transactional
    public ExpMasterClIdResponse save (User user) {

        ExpMasterCl expMasterCl = ExpMasterCl.createData(user);

        expMasterClRepository.save(expMasterCl);
        return ExpMasterClIdResponse.from(expMasterCl.getId());
    }

    @Transactional
    public FindExpMasterClResponse patch (Long id, User user, ExpMasterClPatchRequest request) {
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getCreator().getId());

        expMasterCl.update(request);

        return FindExpMasterClResponse.toDto(expMasterCl);
    }

    @Transactional
    public void delete (Long id, User user) {
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getCreator().getId());
        expMasterCl.disable();
    }

    public List<FindAllExpMasterClResponse> findAll (User user) {
        List<ExpMasterCl> expMasterClList = expMasterClRepository.findAllByActiveTrueAndCreatorId(user.getId());

        return expMasterClList.stream()
                .map(FindAllExpMasterClResponse::toDto)
                .collect(Collectors.toList());
    }

    public FindExpMasterClResponse find (Long id, User user) {
        ExpMasterCl expMasterCl = findById(id);

        validateUser(user.getId(), expMasterCl.getCreator().getId());
        return FindExpMasterClResponse.toDto(expMasterCl);
    }

    @Transactional
    public void defaultSave (User user) {

        ExpMasterCl expMasterCl = ExpMasterCl.builder()
                .material("예시)글로번 커리어 SNS를 통한 수상")
                .emphasis("기획력, 분석력, 프로젝트 관리 능력")
                .expAnalProcess(ExpAnalProcess.PREPARATION)
                .masterClProcess(MasterClProcess.IN_PROGRESS)
                .expAnal("문항 1. 당사에 지원한 동기와 지원 직무를 선택한 이유에 대해 말씀해주세요.\n저는 완전 좋은 사람입니다. 그래서 뽑혀야 합니다.\n근데 알고보면 진짜 더 좋은 사람입니다. 그래서 뽑아야 합니다.\n진짜입니다")
                .masterCl("문항 1. 당사에 지원한 동기와 지원 직무를 선택한 이유에 대해 말씀해주세요.\n저는 완전 좋은 사람입니다. 그래서 뽑혀야 합니다.\n근데 알고보면 진짜 더 좋은 사람입니다. 그래서 뽑아야 합니다.\n진짜입니다")
                .creator(user)
                .active(true)
                .build();

        expMasterClRepository.save(expMasterCl);
    }

    private ExpMasterCl findById (Long id) {
        return expMasterClRepository.findById(id)
                .orElseThrow(() -> new ExpMasterClException(INVALID_INPUT_VALUE));
    }

    private void validateUser(Long userId, Long jobInterviewId) {
        if (!Objects.equals(userId, jobInterviewId)) {
            throw new JobInterviewException(MASTER_CL_FORBIDDEN_OPERATION);
        }
    }
}
