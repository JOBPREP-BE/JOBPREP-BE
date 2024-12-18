package io.dev.jobprep.domain.study.domain.entity;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_DELETED_STUDY;
import static io.dev.jobprep.exception.code.ErrorCode400.NON_GATHERED_USER;
import static io.dev.jobprep.exception.code.ErrorCode400.STUDY_GATHERED_USER_EXCEED;

import io.dev.jobprep.common.base.BaseTimeEntity;
import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import io.dev.jobprep.domain.study.domain.entity.enums.StudyStatus;
import io.dev.jobprep.domain.study.exception.StudyException;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "study")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseTimeEntity  {

    private final static int MAX_HEAD_COUNT = 3;
    private final static int MAX_DURATION_WEEKS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // TODO: Unique 컬럼, 중복 검사 필요
    @Column(name = "study_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User creator;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStudy> userStudies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_status", nullable = false)
    private StudyStatus status;

    @Column(name = "head_count", updatable = false)
    private int head_count;

    @Column(name = "duration_weeks", updatable = false)
    private int duration_weeks;

    @Column(name = "google_link")
    private String google_link;

    @Column(name = "discord_link")
    private String discord_link;

    @Column(name = "kakao_link")
    private String kakao_link;

    @Builder
    private Study(
        Long id,
        User creator,
        String name,
        Position position,
        String google_link,
        String discord_link,
        String kakao_link
    ) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.position = position;
        this.status = StudyStatus.RECRUITING;
        this.head_count = MAX_HEAD_COUNT;
        this.duration_weeks = MAX_DURATION_WEEKS;
        this.google_link = google_link;
        this.discord_link = discord_link;
        this.kakao_link = kakao_link;
    }

    public void join(User user) {
        validateAvailableJoin();
        UserStudy userStudy = UserStudy.of(user, this);
        userStudies.add(userStudy);
    }

    public void kickOut(UserStudy userStudy) {
        userStudies.remove(userStudy);
    }

    public void close() {
        status.validateAvailableRecruitment();
        this.status = StudyStatus.RECRUITMENT_CLOSED;
    }

    public void finish() {
        status.validateAvailableFinish();
        this.status = StudyStatus.FINISHED;
    }

    public void delete(User user) {
        validateAvailableDelete(user);
        super.delete();
    }

    public void modifyLink(User user, String field, String link) {
        // TODO: 어드민 검증 로직 위치 변경?
        validateAdmin(user);

        // TODO: URL 유효성 검사 로직 굳이 추가해야 하나?

        switch (field) {
            case "google" -> this.google_link = link;
            case "discord" -> this.discord_link = link;
            case "kakao" -> this.kakao_link = link;
            default -> throw new IllegalArgumentException("존재하지 않는 필드입니다.");
        }
    }

    // TODO: 매일 자정마다 자동 삭제를 진행하는 스케줄러 구현
    public void deleteForInternal() {
        validateAlreadyDeleted();
        finish();
        super.delete();
    }

    public int getUserAmountOfGathered() {
        return userStudies.size();
    }

    public boolean isCreator(Long userId) {
        return creator.getId().equals(userId);
    }

    private void validateAvailableJoin() {
        status.validateAvailableRecruitment();
        validateHeadCount();
    }

    private void validateAvailableKickOut(UserStudy userStudy) {
        if (!userStudies.contains(userStudy)) {
            throw new StudyException(NON_GATHERED_USER);
        }
    }

    private void validateHeadCount() {
        if (isFullHeadCount()) {
            close();
            throw new StudyException(STUDY_GATHERED_USER_EXCEED);
        }
    }

    private boolean isFullHeadCount() {
        return getUserAmountOfGathered() >= MAX_HEAD_COUNT;
    }

    private void validateAvailableDelete(User user) {
        validateAdmin(user);
        validateAlreadyDeleted();
    }

    private void validateAlreadyDeleted() {
        if (getDeletedAt() != null) {
            throw new StudyException(ALREADY_DELETED_STUDY);
        }
    }

    private void validateAdmin(User user) {
        // TODO: user role == ADMIN 검증해야 함
        user.validateAdmin();
    }
}
