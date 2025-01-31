package io.dev.jobprep.domain.essentialMaterial.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "필수 요소 수정 요청에 대한 응답")
@AllArgsConstructor
@Getter
public class EssentialMaterialUpdateAPIResponse {
    @Schema(description = "수정 요청할 필수 요소 내용", example = "토익:numn15L4W2Z3 \n 오픽:N34FS52\n학점:1.5", implementation = String.class)
    private String content;
    public static EssentialMaterialUpdateAPIResponse from(String content){
        return new EssentialMaterialUpdateAPIResponse(content);
    }
}