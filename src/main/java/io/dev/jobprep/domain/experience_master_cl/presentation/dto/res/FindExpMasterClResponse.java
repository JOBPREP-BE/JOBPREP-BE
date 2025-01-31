package io.dev.jobprep.domain.experience_master_cl.presentation.dto.res;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindExpMasterClResponse {
    @Schema(description = "자소서 ID")
    private final Long id;
    @Schema(description = "경험 소재")
    private final String material;
    @Schema(description = "강조 역량")
    private final String emphasis;
    @Schema(description = "경험 분석 진행 상황")
    private final String expAnalProcess;
    @Schema(description = "마스터 자소서 진행 상황")
    private final String masterClProcess;
    @Schema(description = "경험 분석")
    private final String expAnal;
    @Schema(description = "마스터 자소서")
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
