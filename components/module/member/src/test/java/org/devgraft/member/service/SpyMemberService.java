package org.devgraft.member.service;

import org.devgraft.member.domain.GenderEnum;

import java.time.LocalDateTime;

public class SpyMemberService implements MemberService {
    public MemberJoinRequest argument_request;
    public String argument_id;
    public MemberGetResponse getMember_returnValue;
    public String modifyMember_id_argument;
    public MemberModifyRequest modifyMember_request_argument;
    public String getAuthenticationInfo_argument;

    @Override
    public MemberJoinResponse joinMember(MemberJoinRequest request) {
        this.argument_request = request;
        return new MemberJoinResponse(request.getNickName(), request.getId(), request.getGender(), LocalDateTime.of(2022, 3, 25, 9, 32, 0));
    }

    @Override
    public MemberGetResponse getMember(String id) {
        this.argument_id = id;
        return getMember_returnValue;
    }

    @Override
    public void modifyMember(String id, MemberModifyRequest request) {
        this.modifyMember_id_argument = id;
        this.modifyMember_request_argument = request;
    }

    @Override
    public MemberAuthenticationInfoGetResponse getAuthenticationInfo(String id) {
        getAuthenticationInfo_argument = id;
        return new MemberAuthenticationInfoGetResponse("id",
                "password",
                "nickName",
                GenderEnum.Male,
                LocalDateTime.of(2022, 2, 2, 10, 10, 10),
                LocalDateTime.of(2022, 2, 2, 10, 10, 10)
        );
    }
}
