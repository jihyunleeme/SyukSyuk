package org.devgraft.auth.service;

import org.devgraft.client.member.MemberAuthenticationInfoGetResponse;
import org.devgraft.client.member.MemberClient;
import org.devgraft.client.member.MemberGetResponse;
import org.devgraft.simple.provider.SHA256Provider;
import org.devgraft.simple.provider.UUIDProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final MemberClient memberClient;
    private final UUIDProvider uuidProvider;
    private final SHA256Provider sha256Provider;

    @Override
    public CryptGenerateResponse generateCrypt() {
        String uniqId = uuidProvider.randomUUID().toString();
        String encrypt = sha256Provider.encrypt(uniqId);
        return new CryptGenerateResponse(encrypt);
    }

    @Override
    public MemberAuthenticationResponse authenticationMember(MemberAuthenticationRequest request,
                                                             String crypt) {
        log.info("사용자 조회(id: {})", request.getId());
        MemberAuthenticationInfoGetResponse response = memberClient.getMemberAuthenticationInfo(request.getId());
        String base64Password = Base64.getEncoder().encodeToString(response.getPassword().getBytes(StandardCharsets.UTF_8));
        String encrypt = sha256Provider.encrypt(base64Password, crypt);
        boolean passwordEquals = encrypt.equals(request.getPassword());
        log.info("암호화 패스워드 비교\nrequest.password: [{}]\nencryptPassword: [{}]\n동일여부: {}", request.getPassword(), encrypt, passwordEquals);
        if (!passwordEquals) {
            RuntimeException e = new RuntimeException("패스워드 검증이 실패하였습니다.");
            e.printStackTrace();
            throw e;
        }

        return new MemberAuthenticationResponse(response.getId(), response.getNickName(), response.getGender());
    }
}
