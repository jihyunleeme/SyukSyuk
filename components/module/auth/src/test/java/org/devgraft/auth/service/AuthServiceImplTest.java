package org.devgraft.auth.service;

import org.devgraft.auth.client.SpyMemberClient;
import org.devgraft.auth.provider.StubSHA256Provider;
import org.devgraft.auth.provider.StubUUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest {
    AuthServiceImpl authServiceImpl;
    SpyMemberClient spyMemberClient;
    StubUUIDProvider stubUUIDProvider;
    StubSHA256Provider stubSHA256Provider;

    @BeforeEach
    void setUp() {
        spyMemberClient = new SpyMemberClient();
        stubUUIDProvider = new StubUUIDProvider();
        stubSHA256Provider = new StubSHA256Provider();
        authServiceImpl = new AuthServiceImpl(spyMemberClient, stubUUIDProvider, stubSHA256Provider);
    }

    @Test
    void generateCrypt_returnValue() throws Exception {
        CryptGenerateResponse response = authServiceImpl.generateCrypt();

        assertThat(stubSHA256Provider.encrypt_argument).isEqualTo(stubUUIDProvider.randomUUID().toString());
        assertThat(response.getCrypt()).isEqualTo(stubSHA256Provider.encrypt_returnValue);
    }

    @Test
    void authenticationMember_returnValue() throws Exception {
        MemberAuthenticationRequest givenRequest = new MemberAuthenticationRequest("id", stubSHA256Provider.encrypt_hmac_returnValue);
        String givenCrypt = "crypt";

        MemberAuthenticationResponse response = authServiceImpl.authenticationMember(givenRequest, givenCrypt);

        assertThat(response.getId()).isEqualTo(spyMemberClient.getMemberAuthenticationInfo_returnValue.getId());
        assertThat(response.getNickName()).isEqualTo(spyMemberClient.getMemberAuthenticationInfo_returnValue.getNickName());
        assertThat(response.getGender()).isEqualTo(spyMemberClient.getMemberAuthenticationInfo_returnValue.getGender());
    }

    @Test
    void authenticationMember_passesIdToMemberClient() throws Exception {
        MemberAuthenticationRequest givenRequest = new MemberAuthenticationRequest("id", stubSHA256Provider.encrypt_hmac_returnValue);
        String givenCrypt = "crypt";

        authServiceImpl.authenticationMember(givenRequest, givenCrypt);

        assertThat(spyMemberClient.getMemberAuthenticationInfo_argument).isEqualTo(givenRequest.getId());
    }

    @Test
    void authenticationMember_requestPassword_EqualTo_CryptPassword() throws Exception {
        MemberAuthenticationRequest givenRequest = new MemberAuthenticationRequest("id", stubSHA256Provider.encrypt_hmac_returnValue);
        String givenCrypt = "crypt";

        authServiceImpl.authenticationMember(givenRequest, givenCrypt);

        assertThat(stubSHA256Provider.encrypt_hmac_text_argument)
                .isEqualTo(Base64.getEncoder().encodeToString(spyMemberClient.getMemberAuthenticationInfo_returnValue.getPassword()
                        .getBytes(StandardCharsets.UTF_8)));
        assertThat(stubSHA256Provider.encrypt_hmac_crypt_argument).isEqualTo(givenCrypt);
    }
}