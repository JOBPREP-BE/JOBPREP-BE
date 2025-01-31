package io.dev.jobprep.domain.essentialMaterial.presentation.dto.req;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Schema(description = "필수 요소 수정 요청")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EssentialMaterialIUpdateAPIRequest {
    @Schema(description = "수정 요청할 필수 요소 내용", example = "토익:numn15L4W2Z3 \n 오픽:N34FS52\n학점:1.5", implementation = String.class)
    private String content;
    public static EssentialMaterialIUpdateAPIRequest from(String content){
        return new EssentialMaterialIUpdateAPIRequest(content);
    }
}
