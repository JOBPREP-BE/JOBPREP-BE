package io.dev.jobprep.domain.study.domain.entity;

import static io.dev.jobprep.exception.code.ErrorCode400.STUDY_WEEK_NUMBER_EXCEED;
import static io.dev.jobprep.exception.code.ErrorCode404.STUDY_NOT_FOUND;

import io.dev.jobprep.domain.study.exception.StudyException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "study_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySchedule {

    private final int MAX_WEEK_NUMBER = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", updatable = false)
    private Study study;

    @Future
    @Column(name = "start_date")
    private LocalDateTime start_date;

    @Column(name = "week_number")
    private int week_number;

    @Builder
    private StudySchedule(Long id, Study study, LocalDateTime start_date, int week_number) {
        this.id = id;
        this.study = study;
        // TODO: 주차별 시작 일자가 순차적으로 커지는지를 검증해야 할까?
        this.start_date = start_date;
        this.week_number = week_number;
        validateStudy(study);
        validateWeekNumber(week_number);
    }

    public void modify(LocalDateTime startDate) {
        validateDate(startDate, week_number);
        this.start_date = startDate;
    }

    private void validateStudy(Study study) {
        if (!this.study.getId().equals(study.getId())) {
            throw new StudyException(STUDY_NOT_FOUND);
        }
    }

    private void validateWeekNumber(int week_number) {
        if (week_number > MAX_WEEK_NUMBER) {
            throw new StudyException(STUDY_WEEK_NUMBER_EXCEED);
        }
    }

    private void validateDate(LocalDateTime startDate, int weekNumber) {

    }
}
