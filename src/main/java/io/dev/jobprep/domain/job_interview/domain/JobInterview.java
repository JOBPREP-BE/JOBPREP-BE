package io.dev.jobprep.domain.job_interview.domain;

import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class JobInterview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String question;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobInterviewCategory category = JobInterviewCategory.ABILITY;

    @Column(length = 1500)
    private String answer;

    public void update(PutJobInterviewRequest request) {
        question = request.getQuestion();
        category = request.getCategory();
        answer = request.getAnswer();
    }

    private JobInterview(Long id, String question, JobInterviewCategory category, String answer) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.answer = answer;
    }
}
