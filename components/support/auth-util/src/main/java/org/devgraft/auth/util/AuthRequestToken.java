package org.devgraft.auth.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthRequestToken {
    private final String accessToken;
    private final String refreshToken;
}
