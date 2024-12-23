package io.dev.jobprep.system.internal.whitelist.infrastructure;

import io.dev.jobprep.system.internal.whitelist.domain.entity.WhiteList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListJpaRepository extends JpaRepository<WhiteList, Long> {

    @Query("select wl from WhiteList wl where wl.accessIp = :accessIp")
    public Optional<WhiteList> findByAccessIp(String accessIp);

}
