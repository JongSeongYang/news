package com.someverything.news.domain.member;

import com.someverything.news.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Auth_Code")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthCode extends BaseEntity {
    /** PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** member_id(FK) */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /** 인증 코드 */
    private String authCode;

    /** 인증 방법 코드 */
    private String authMethodCode;

    /** 인증 횟수 */
    private Integer authCnt;

    /** 인증 성공 여부 */
    private String isAuthSuccess;
}
