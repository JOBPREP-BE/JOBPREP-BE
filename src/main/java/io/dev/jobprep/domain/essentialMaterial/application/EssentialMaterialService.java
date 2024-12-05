package io.dev.jobprep.domain.essentialMaterial.application;

import io.dev.jobprep.domain.essentialMaterial.Exception.EssentialMaterialException;
import io.dev.jobprep.domain.essentialMaterial.domain.EssentialMaterial;
import io.dev.jobprep.domain.essentialMaterial.infrastructure.EssentialMaterialRepository;
import io.dev.jobprep.domain.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.dev.jobprep.exception.code.ErrorCode404.ESSENTIAL_MATERIAL_NOT_FOUND;

@Service
public class EssentialMaterialService {

    private final EssentialMaterialRepository essentialMaterialRepository;
    @Autowired
    public EssentialMaterialService(EssentialMaterialRepository essentialMaterialRepository){
        this.essentialMaterialRepository = essentialMaterialRepository;
    }


    @Transactional
    public String get(User user) {
        return this.createIfNoContent(user).getEssentialMaterial();
    }

    @Transactional
    public String update(User user, String content){
        return this.createIfNoContent(user).update(content);
    }

    //user에게 할당된 E.M이 존재하지 않는다면 update 요청시 새로 만들어서 줌.
    private EssentialMaterial createIfNoContent(User user){
        EssentialMaterial originContent;
        try {
            originContent = essentialMaterialRepository.findByUser(user)
                    .orElseThrow(() -> new EssentialMaterialException(ESSENTIAL_MATERIAL_NOT_FOUND));
        }catch (EssentialMaterialException e){
            originContent = this.create(user);
        }
        return originContent;
    }

    private EssentialMaterial create(User user){
        return essentialMaterialRepository.save(EssentialMaterial.builder()
                .user(user)
                .essentialMaterial("")
                .build());
    }

}
