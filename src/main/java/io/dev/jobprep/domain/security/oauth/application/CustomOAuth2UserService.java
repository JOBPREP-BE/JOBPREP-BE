package io.dev.jobprep.domain.security.oauth.application;

import io.dev.jobprep.domain.security.oauth.domain.OAuthAttributes;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.users.domain.User;
import io.dev.jobprep.domain.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional("transactionManager")
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2UserAttributes);
        User user = fetchAndSaveIfAbsent(attributes);
        log.info("Registered user '{}' with successful OAuth Authentication", user.getEmail());
        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    private User fetchAndSaveIfAbsent(OAuthAttributes attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes cannot be null");
        }
        User user = userRepository.findUserByEmail(attributes.getEmail())
                .orElseGet(attributes::toEntity);
        return userRepository.save(user);
    }
}
