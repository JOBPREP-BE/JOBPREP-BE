package io.dev.jobprep.domain.experience_master_cl.presentation.dto.res;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindAllExpMasterClResponse {
    private final Long id;
    private final String material;
    private final String emphasis;
    private final String expAnalProcess;
    private final String masterClProcess;

    private FindAllExpMasterClResponse(Long id, String material, String emphasis, String expAnalProcess, String masterClProcess) {
        this.id = id;
        this.material = material;
        this.emphasis = emphasis;
        this.expAnalProcess = expAnalProcess;
        this.masterClProcess = masterClProcess;
    }

    public static FindAllExpMasterClResponse toDto (ExpMasterCl expMasterCl) {
        return FindAllExpMasterClResponse.builder()
                .id(expMasterCl.getId())
                .material(expMasterCl.getMaterial())
                .emphasis(expMasterCl.getEmphasis())
                .expAnalProcess(expMasterCl.getExpAnalProcess().getMessage())
                .masterClProcess(expMasterCl.getMasterClProcess().getMessage())
                .build();
    }
}
