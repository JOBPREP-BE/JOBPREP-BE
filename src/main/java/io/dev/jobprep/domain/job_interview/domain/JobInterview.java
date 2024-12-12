package io.dev.jobprep.domain.job_interview.domain;

import io.dev.jobprep.domain.job_interview.domain.enums.JobInterviewCategory;
import io.dev.jobprep.domain.job_interview.presentation.dto.req.PutJobInterviewRequest;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User creator;

    public void update(PutJobInterviewRequest request) {
        switch (request.getField()) {
            case "question" -> question = request.getContent();
            case "category" -> category = JobInterviewCategory.from(request.getContent());
            case "answer" -> answer = request.getContent();
        }
    }

    private JobInterview(Long id, String question, JobInterviewCategory category, String answer, User creator) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.answer = answer;
        this.creator = creator;
    }
}
