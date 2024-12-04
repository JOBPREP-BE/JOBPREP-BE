package io.dev.jobprep.domain.job_interview.domain.enums;

import lombok.Getter;

@Getter
public enum DefaultJobInterview {

    // ABILITY 카테고리 질문들
    INTRODUCE(JobInterviewCategory.ABILITY, "1분 자기소개"),
    WHY_US(JobInterviewCategory.ABILITY, "우리 회사에서 지원자를 뽑아야 하는 이유는 무엇인가요?\n(자신의 강점과 회사에 기여할 수 있는 점을 구체적으로 설명)"),
    MOTIVATION(JobInterviewCategory.ABILITY, "지원 동기에 대해 말해주세요.\n(기업 지원 동기와 직무 지원 동기를 논리적으로 설명)"),
    BEST_EXPERIENCE(JobInterviewCategory.ABILITY, "최선을 다했던 경험이 무엇인가요?\n(끈기 있게 노력한 경험을 구체적으로 설명)"),
    ACTIVITY(JobInterviewCategory.ABILITY, "지원 직무를 위해 어떤 활동을 했나요?\n(직무와 관련된 학습, 프로젝트, 경험 등 참조)"),
    TEAMWORK(JobInterviewCategory.ABILITY, "협력을 통해 문제를 해결한 경험이 있나요?\n(팀 내에서의 협력과 커뮤니케이션 능력 강조)"),
    CONFLICT(JobInterviewCategory.ABILITY, "갈등을 극복한 경험이 있나요?\n(갈등을 해결한 구체적 사례와 그 과정에서 배운 점 강조)"),

    // PERSONALITY 카테고리 질문들
    WEAKNESS(JobInterviewCategory.PERSONALITY, "자신의 약점은 무엇인가요?\n(약점을 극복하기 위한 노력과 개선 의지를 강조)"),
    STRENGTH(JobInterviewCategory.PERSONALITY, "자신장점은 무엇인가요?\n(직무와 연관된 성격상 장점을 참조)"),
    FEEDBACK(JobInterviewCategory.PERSONALITY, "주변에서 지원자를 뭐라고/어떻게 평가하나요?\n(자신의 성향과 대인 관계를 어필)"),
    LEADERSHIP(JobInterviewCategory.PERSONALITY, "리더형인가요? 팔로워형인가요?\n(나의 성향과 실제 경험을 근거로 답변)"),
    HOBBY(JobInterviewCategory.PERSONALITY, "취미 혹은 스트레스를 해소하는 방법은 무엇인가요?\n(스트레스를 원활히 관리하는 인재임을 어필)"),
    FINAL_WORDS(JobInterviewCategory.PERSONALITY, "마지막으로 하고 싶은 말이 있나요?\n(면접 마지막에 자신을 어필할 기회를 최대한 활용)");

    private final JobInterviewCategory category;
    private final String question;

    private DefaultJobInterview(JobInterviewCategory category, String question) {
        this.category = category;
        this.question = question;
    }
}

