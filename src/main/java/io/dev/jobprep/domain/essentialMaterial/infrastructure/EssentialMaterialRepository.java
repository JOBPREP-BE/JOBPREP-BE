package io.dev.jobprep.domain.essentialMaterial.infrastructure;

import io.dev.jobprep.domain.essentialMaterial.domain.EssentialMaterial;
import io.dev.jobprep.domain.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EssentialMaterialRepository extends JpaRepository<EssentialMaterial,Long> {
    Optional<EssentialMaterial> findByUser(User user);

}


