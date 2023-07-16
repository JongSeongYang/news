package com.someverything.news.service;

import com.someverything.news.domain.Member;
import com.someverything.news.dto.MemberDto;

public interface MemberService {
    Member createMember(MemberDto.MemberRequest request);
    Member getMember(Long memberId);
    Member updateMember(Long memberId, MemberDto.MemberRequest request);
    String login(String username, String password);
    void deleteMember(Long memberId, String quitReasonCode, String quitContent);
    void initPassword(Long memberId);
    void changePassword(Long memberId, String currentPassword, String newPassword);
}
