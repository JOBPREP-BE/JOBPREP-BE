package io.dev.jobprep.domain.essentialMaterial.presentation.dto;

import io.dev.jobprep.common.swagger.template.EssentialMaterialSwagger;
import io.dev.jobprep.domain.essentialMaterial.application.EssentialMaterialService;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.req.EssentialMaterialIUpdateAPIRequest;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.res.EssentialMaterialGetAPIResponse;
import io.dev.jobprep.domain.essentialMaterial.presentation.dto.res.EssentialMaterialUpdateAPIResponse;
import io.dev.jobprep.domain.users.application.UserCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/essential-material")
public class EssentialMaterialController implements EssentialMaterialSwagger {
    private final EssentialMaterialService essentialMaterialService;
    private final UserCommonService userCommonService;

    @GetMapping(value = "/")
    public ResponseEntity<EssentialMaterialGetAPIResponse> get(@RequestParam(required = false) Long userId){
        String material = essentialMaterialService.get(userCommonService.getUserWithId(userId));
        return ResponseEntity.ok(new EssentialMaterialGetAPIResponse(material));
    }

    @PatchMapping(value ="/update")
    public ResponseEntity<EssentialMaterialUpdateAPIResponse> update(@RequestParam(required = false) Long userId,
                                                                     @RequestBody EssentialMaterialIUpdateAPIRequest req){
        String material = essentialMaterialService.update(userCommonService.getUserWithId(userId),req.getContent());
        return ResponseEntity.ok(new EssentialMaterialUpdateAPIResponse(material));
    }
}
