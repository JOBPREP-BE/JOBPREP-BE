package io.dev.jobprep.domain.study.domain.entity;

import static io.dev.jobprep.exception.code.ErrorCode400.ALREADY_DELETED_STUDY;

import io.dev.jobprep.common.base.BaseTimeEntity;
import io.dev.jobprep.domain.study.domain.entity.enums.Position;
import io.dev.jobprep.domain.study.domain.entity.enums.StudyStatus;
import io.dev.jobprep.domain.study.exception.StudyException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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

    private String name;

    // TODO: 추후에 스터디 creator_info 추가
    @Column(name = "user_id")
    private Long userId;

    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_status", nullable = false)
    private StudyStatus status;

    @Column(name = "head_count", updatable = false)
    private int head_count;

    @Column(name = "duration_weeks", updatable = false)
    private int duration_weeks;

    @Pattern(
        regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$",
        message = "Invalid URL format"
    )
    @Column(name = "google_link")
    private String google_link;

    @Pattern(
        regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$",
        message = "Invalid URL format"
    )
    @Column(name = "discord_link")
    private String discord_link;

    @Pattern(
        regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$",
        message = "Invalid URL format"
    )
    @Column(name = "kakao_link")
    private String kakao_link;

    @Builder
    private Study(
        Long id,
        Long creatorId,
        String name,
        Position position,
        String google_link,
        String discord_link,
        String kakao_link
    ) {
        this.id = id;
        this.userId = creatorId;
        this.name = name;
        this.position = position;
        this.status = StudyStatus.RECRUITING;
        this.head_count = MAX_HEAD_COUNT;
        this.duration_weeks = MAX_DURATION_WEEKS;
        this.google_link = google_link;
        this.discord_link = discord_link;
        this.kakao_link = kakao_link;
    }

    public void close() {
        status.validateAvailableRecruitment();
        this.status = StudyStatus.RECRUITMENT_CLOSED;
    }

    public void finish() {
        status.validateAvailableFinish();
        this.status = StudyStatus.FINISHED;
    }

    public void modifyLink(Long userId, String field, String link) {
        // TODO: 어드민 검증 로직 위치 변경?
        validateAdmin(userId);

        // TODO: URL 유효성 검사 로직 추가

        switch (field) {

            case "google":
                this.google_link = link;
                break;

            case "discord":
                this.discord_link = link;
                break;

            case "kakao":
                this.kakao_link = link;
                break;

            default:
                throw new IllegalArgumentException("존재하지 않는 필드입니다.");
        }
    }

    // TODO: User 엔티티 생성 시, userId -> tokenUser로 변경

    public void delete(Long userId) {
        validateAvailableDelete(userId);
        delete();
    }

    public void deleteForInternal() {
        // TODO: 해당 스터디가 마감일까지 모집 인원을 채우지 못했는지 검증
        // TODO: 해당 스터디가 3주차 진행을 완료했는지 검증
        validateAlreadyDeleted();
    }

    public void validateAvailableJoin() {
        status.validateAvailableRecruitment();
    }

    public void validateAvailableDelete(Long userId) {
        validateAdmin(userId);
        validateAlreadyDeleted();
    }

    public void validateAlreadyDeleted() {
        if (getDeletedAt() != null) {
            throw new StudyException(ALREADY_DELETED_STUDY);
        }
    }

    public void validateAdmin(Long userId) {
        // TODO: user role == ADMIN 검증해야 함
    }
}
