package io.dev.jobprep.domain.applicationstatus.domain.entity;

import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProcess;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProgress;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.util.LocalDateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "application_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationStatus {

    private static final int INIT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User creator;

    @Column(name = "company")
    private String company;

    @Column(name = "position")
    private String position;

    @Column(name = "application_progress")
    private ApplicationProgress progress;

    @Column(name = "application_process")
    private ApplicationProcess process;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "url")
    private String url;

    @Column(name = "cover_letter")
    private String coverLetter;

    @Builder
    private ApplicationStatus(
        Long id,
        User creator,
        String company,
        String position,
        ApplicationProgress progress,
        ApplicationProcess process,
        LocalDateTime applicationDate,
        LocalDateTime dueDate,
        String url,
        String coverLetter
    ) {
        this.id = id;
        this.creator = creator;
        this.company = company;
        this.position = position;
        this.progress = progress;
        this.process = process;
        this.applicationDate = applicationDate;
        this.dueDate = dueDate;
        this.url = url;
        this.coverLetter = coverLetter;
    }

    public void modify(String field, String newVal) {

        switch (field) {
            case "company" -> this.company = newVal;
            case "position" -> this.position = newVal;
            case "progress" -> this.progress = ApplicationProgress.from(newVal);
            case "process" -> this.process = ApplicationProcess.from(newVal);
            case "applicationDate" -> this.applicationDate = LocalDateTimeConverter.convertToUtcLDT(newVal);
            case "dueDate" -> this.dueDate = LocalDateTimeConverter.convertToUtcLDT(newVal);
            case "url" -> this.url = newVal;
            case "coverLetter" -> this.coverLetter = newVal;
        }
    }

    private static LocalDateTime calculateDueDate(LocalDateTime applicationDate) {
        return applicationDate.plusDays(INIT);
    }

    public static ApplicationStatus of(
        User creator,
        String company,
        String position,
        ApplicationProgress progress,
        ApplicationProcess process,
        String url,
        String coverLetter
    ) {
        LocalDateTime today = LocalDateTime.now();
        return ApplicationStatus.builder()
            .creator(creator)
            .company(company)
            .position(position)
            .progress(progress)
            .process(process)
            .applicationDate(today)
            .dueDate(calculateDueDate(today))
            .url(url)
            .coverLetter(coverLetter)
            .build();
    }
}
