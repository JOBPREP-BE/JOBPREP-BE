package io.dev.jobprep.domain.essentialMaterial.presentation.dto.res;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "필수 요소 열람 요청에 대한 응답")
@AllArgsConstructor
@Getter
public class EssentialMaterialGetAPIResponse {
    @Schema(description = "필수 요소 내용", example = "토익:numn15LLC2Z3 \n 오픽:N347S52\n학점:8.5", implementation = String.class)
    private String content;
    public static EssentialMaterialGetAPIResponse from(String content){
        return new EssentialMaterialGetAPIResponse(content);
    }
}


