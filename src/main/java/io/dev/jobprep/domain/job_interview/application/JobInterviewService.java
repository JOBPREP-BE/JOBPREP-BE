package io.dev.jobprep.domain.job_interview.application;

import io.dev.jobprep.domain.job_interview.domain.JobInterview;
import io.dev.jobprep.domain.job_interview.domain.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.infrastructure.JobInterviewRepository;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.job_interview.presentation.dto.res.FindJobInterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;

    public Long saveJobInterview () {
        JobInterview jobInterview = JobInterview.builder()
                .question("")
                .category(JobInterviewCategory.PERSONALITY)
                .answer("")
                .build();

        jobInterviewRepository.save(jobInterview);
        return jobInterview.getId();
    }

    public FindJobInterviewResponse update (PutJobInterviewRequest request, Long id) {
        JobInterview savedEntity = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("면접 데이터를 찾을 수 없습니다."));

        savedEntity.update(request);
        jobInterviewRepository.save(savedEntity);

        return FindJobInterviewResponse.from(savedEntity);
    }

    public Long delete(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("면접 데이터를 찾을 수 없습니다."));
        jobInterviewRepository.delete(jobInterview);
        return id;
    }

    public List<FindJobInterviewResponse> find() {
        List<JobInterview> jobInterviewList = jobInterviewRepository.findAll();
        return jobInterviewList
                .stream()
                .map(FindJobInterviewResponse::from)
                .collect(Collectors.toList());
    }

    public void initJobInterview(Long memberId) {
        HashMap<JobInterviewCategory, List<String>> initQuestion = initQuestion();
        for (JobInterviewCategory category : initQuestion.keySet()) {
            List<String> questionList = initQuestion.get(category);

            for (String question : questionList) {
                JobInterview jobInterview = JobInterview.builder()
                        .question(question)
                        .category(category)
                        .answer("")
                        .build();
                jobInterviewRepository.save(jobInterview);
            }
        }
    }

    private HashMap<JobInterviewCategory, List<String>> initQuestion() {
        HashMap<JobInterviewCategory, List<String>> question = new HashMap<>();

        List<String> abilityQuestion = new ArrayList<>();
        abilityQuestion.add("1분 자기소개");
        abilityQuestion.add("우리 회사에서 지원자를 뽑아야 하는 이유는 무엇인가요?" + System.lineSeparator() + "(자신의 강점과 회사에 기여할 수 있는 점을 구체적으로 설명)");
        abilityQuestion.add("지원 동기에 대해 말해주세요." + System.lineSeparator() + "(기업 지원 동기와 직무 지원 동기를 논리적으로 설명)");
        abilityQuestion.add("최선을 다했던 경험이 무엇인가요?" + System.lineSeparator() + "(끈기 있게 노력한 경험을 구체적으로 설명)");
        abilityQuestion.add("지원 직무를 위해 어떤 활동을 했나요?" + System.lineSeparator() + "(직무와 관련된 학습, 프로젝트, 경험 등 참조)");
        abilityQuestion.add("협력을 통해 문제를 해결한 경험이 있나요?" + System.lineSeparator() + "(팀 내에서의 협력과 커뮤니케이션 능력 강조)");
        abilityQuestion.add("갈등을 극복한 경험이 있나요?" + System.lineSeparator() + "(갈등을 해결한 구체적 사례와 그 과정에서 배운 점 강조)");

        question.put(JobInterviewCategory.ABILITY, abilityQuestion);

        List<String> personalityQuestion = new ArrayList<>();
        personalityQuestion.add("자신의 약점은 무엇인가요?" + System.lineSeparator() + "(약점을 극복하기 위한 노력과 개선 의지를 강조)");
        personalityQuestion.add("자신장점은 무엇인가요?" + System.lineSeparator() + "(직무와 연관된 성격상 장점을 참조)");
        personalityQuestion.add("주변에서 지원자를 뭐라고/어떻게 평가하나요?" + System.lineSeparator() + "(자신의 성향과 대인 관계를 어필");
        personalityQuestion.add("리더형인가요? 팔로워형인가요?" + System.lineSeparator() + "(나의 성향과 실제 경험을 근거로 답변)");
        personalityQuestion.add("취미 혹은 스트레스를 해소하는 방법은 무엇인가요?" + System.lineSeparator() + "(스트레스를 원활히 관리하는 인재임을 어필");
        personalityQuestion.add("마지막으로 하고 싶은 말이 있나요?"  + System.lineSeparator() + "(면접 마지막에 자신을 어필할 기회를 최대한 활용)");

        question.put(JobInterviewCategory.PERSONALITY, personalityQuestion);

        return question;
    }

}
