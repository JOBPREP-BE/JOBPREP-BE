package io.dev.jobprep.domain.experience_master_cl.domain;

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
public class ExpMasterCl {

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


    private static final String BLANK = "";

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

    public void update(String field, ExpMasterClPatchRequest request) {
        switch (field) {
            case "material" -> this.material = request.getNewVal();
            case "emphasis" -> this.emphasis = request.getNewVal();
            case "expAnalProcess" -> this.expAnalProcess = ExpAnalProcess.from(request.getNewVal());
            case "masterClProcess" -> this.masterClProcess = MasterClProcess.from(request.getNewVal());
            case "expAnal" -> this.expAnal = request.getNewVal();
            case "masterCl" -> this.masterCl = request.getNewVal();
            default -> throw new ExpMasterClException(INVALID_INPUT_VALUE);
        }
    }

    public static ExpMasterCl createData(User user) {
        return ExpMasterCl.builder()
                .expAnalProcess(ExpAnalProcess.PREPARATION)
                .masterClProcess(MasterClProcess.PREPARATION)
                .creator(user)
                .active(true)
                .build();
    }

    public void disable() {
        if (!active) {
            throw new ExpMasterClException(INVALID_INPUT_VALUE);
        }
        active = false;
    }
}
