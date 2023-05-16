package com.someverything.news.domain.member;

import com.someverything.news.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Member_Detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDetail extends BaseEntity {
    /** PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** member_id(FK) */
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /** 회원 명 */
    private String memberNm;

    /** 생년월일 */
    private String birth;

    /** 주민번호 */
    private String residentNumber;

    /** 전화 */
    private String tel;

    /** 휴대폰 */
    private String mobileNumber;

    /** 이메일 */
    private String email;

    /** 성별 구분 코드 */
    private String genderCode;

    /** 국적 구분 코드 */
    private String nationCode;

    /** 개인정보 수정 일시 */
    private LocalDateTime infoUpdateDt;
}
