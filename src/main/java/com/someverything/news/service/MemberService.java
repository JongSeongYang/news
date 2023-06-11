package com.someverything.news.service;

import com.someverything.news.domain.Member;
import com.someverything.news.dto.MemberDto;

public interface MemberService {
    Member createMember(MemberDto.MemberRequest request);
    Member getMember(Long memberId);
    void deleteMember(Long memberId);
}
