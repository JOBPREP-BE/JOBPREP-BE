package io.dev.jobprep.domain.experience_master_cl.infrastructure;

import io.dev.jobprep.domain.experience_master_cl.domain.ExpMasterCl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpMasterClRepository extends JpaRepository<ExpMasterCl, Long> {
    List<ExpMasterCl> findAllByActiveTrueAndCreatorId(Long id);
}
