package io.dev.jobprep.domain.experience_master_cl.presentation.dto.res;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindExpMasterClResponse {
    private final Long id;
    private final String material;
    private final String emphasis;
    private final String expAnalProcess;
    private final String masterClProcess;
    private final String expAnal;
    private final String masterCl;

    private FindExpMasterClResponse(Long id, String material, String emphasis, String expAnalProcess, String masterClProcess, String expAnal, String masterCl) {
        this.id = id;
        this.material = material;
        this.emphasis = emphasis;
        this.expAnalProcess = expAnalProcess;
        this.masterClProcess = masterClProcess;
        this.expAnal = expAnal;
        this.masterCl = masterCl;
    }

    public static FindExpMasterClResponse toDto(ExpMasterCl expMasterCl) {
        return FindExpMasterClResponse.builder()
                .id(expMasterCl.getId())
                .material(expMasterCl.getMaterial())
                .emphasis(expMasterCl.getEmphasis())
                .expAnalProcess(expMasterCl.getExpAnalProcess().getMessage())
                .masterClProcess(expMasterCl.getMasterClProcess().getMessage())
                .expAnal(expMasterCl.getExpAnal())
                .masterCl(expMasterCl.getMasterCl())
                .build();
    }
}
