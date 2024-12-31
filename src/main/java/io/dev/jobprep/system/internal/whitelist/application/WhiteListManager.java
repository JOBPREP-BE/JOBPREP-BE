package io.dev.jobprep.system.internal.whitelist.application;

import io.dev.jobprep.system.internal.whitelist.infrastructure.WhiteListJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WhiteListManager {

    private final WhiteListJpaRepository whiteListRepository;

    public boolean isBelongWhiteList(String accessIp) {
        return whiteListRepository.findByAccessIp(accessIp)
            .isPresent();
    }

}
