package org.devgraft.auth.store.service;

import java.util.Optional;

public class SpyAuthTokenService implements AuthTokenService {
    public TokenGenerateRequest generateToken_argument;
    public TokenGenerateResponse generateToken_returnValue = new TokenGenerateResponse("accessToken1", "refresh");
    public String deleteToken_accessToken_argument;
    public String deleteToken_refreshToken_argument;

    @Override
    public TokenGenerateResponse generateToken(TokenGenerateRequest request) {
        this.generateToken_argument = request;
        return generateToken_returnValue;
    }

    @Override
    public void deleteToken(String accessToken, String refreshToken) {
        this.deleteToken_accessToken_argument = accessToken;
        this.deleteToken_refreshToken_argument = refreshToken;
    }

    @Override
    public Optional<AuthDataInformation> getAuthDataInformation(String accessToken) {
        return Optional.empty();
    }

    @Override
    public Optional<TokenReIssueResponse> reIssueToken(String accessToken, String refreshToken) {
        return Optional.empty();
    }
}
