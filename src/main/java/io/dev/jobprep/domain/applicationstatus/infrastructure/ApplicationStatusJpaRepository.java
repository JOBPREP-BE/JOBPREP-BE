package io.dev.jobprep.domain.applicationstatus.infrastructure;

import io.dev.jobprep.domain.applicationstatus.domain.entity.ApplicationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationStatusJpaRepository extends JpaRepository<ApplicationStatus, Long> {

    @Query("select ats from ApplicationStatus ats where ats.creator.id = :userId")
    List<ApplicationStatus> findMyAppStatusAll(Long userId);
}
