package io.dev.jobprep.domain.applicationstatus.infrastructure;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationStatusJpaRepository extends JpaRepository<ApplicationStatus, Long>,
    ApplicationStatusRepositoryCustom {

}
