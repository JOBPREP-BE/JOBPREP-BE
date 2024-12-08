package io.dev.jobprep.domain.experience_master_cl.domain;

import io.dev.jobprep.common.base.BaseTimeEntity;
import io.dev.jobprep.domain.experience_master_cl.domain.enums.ExpAnalProcess;
import io.dev.jobprep.domain.experience_master_cl.domain.enums.MasterClProcess;
import io.dev.jobprep.domain.experience_master_cl.exception.ExpMasterClException;
import io.dev.jobprep.domain.experience_master_cl.presentation.dto.req.ExpMasterClPatchRequest;
import io.dev.jobprep.domain.users.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.dev.jobprep.exception.code.ErrorCode400.*;

@Getter
@Entity
@Table(name = "experience_master_cl")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpMasterCl extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String material;

    @Column(length = 100)
    private String emphasis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpAnalProcess expAnalProcess;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MasterClProcess masterClProcess;

    @Column
    private String expAnal;

    @Column
    private String masterCl;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User creator;


    private ExpMasterCl(Long id, String material, String emphasis, ExpAnalProcess expAnalProcess, MasterClProcess masterClProcess, String expAnal, String masterCl, boolean active, User creator) {
        this.id = id;
        this.material = material;
        this.emphasis = emphasis;
        this.expAnalProcess = expAnalProcess;
        this.masterClProcess = masterClProcess;
        this.expAnal = expAnal;
        this.masterCl = masterCl;
        this.active = active;
        this.creator = creator;
    }

    public void update(ExpMasterClPatchRequest request) {
        switch (request.getField()) {
            case "material" -> this.material = request.getContent();
            case "emphasis" -> this.emphasis = request.getContent();
            case "expAnalProcess" -> this.expAnalProcess = ExpAnalProcess.from(request.getContent());
            case "masterClProcess" -> this.masterClProcess = MasterClProcess.from(request.getContent());
            case "expAnal" -> this.expAnal = request.getContent();
            case "masterCl" -> this.masterCl = request.getContent();
            default -> throw new ExpMasterClException(INVALID_INPUT_VALUE);
        }
    }

    public void disable() {
        if (!active) {
            throw new ExpMasterClException(INVALID_INPUT_VALUE);
        }
        active = false;
    }
}
