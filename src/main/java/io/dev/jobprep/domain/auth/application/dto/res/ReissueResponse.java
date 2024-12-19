package io.dev.jobprep.domain.auth.application.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueResponse {

    private final String accessToken;

    public static ReissueResponse from(String accessToken) {
        return new ReissueResponse(accessToken);
    }

}
