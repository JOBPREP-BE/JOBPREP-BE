package io.dev.jobprep.security.oauth.application;

import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 조회
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // PrincipalDetails 생성 후 반환
        return new PrincipalDetails(user, null, null);
    }
}