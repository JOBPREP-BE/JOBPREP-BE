package io.dev.jobprep.domain.study.domain.entity;

import io.dev.jobprep.domain.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_study")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Builder
    private UserStudy(Long id, User user, Study study) {
        this.id = id;
        this.user = user;
        this.study = study;
    }

    public static UserStudy of(User user, Study study) {
        return UserStudy.builder()
            .user(user)
            .study(study)
            .build();
    }
}
