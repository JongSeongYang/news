package com.someverything.news.domain;

import com.someverything.news.global.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Member_Quit")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberQuit extends BaseEntity {
    /** 회원 번호 */
    @Id
    private Long id;

    /** 회원 탈퇴 사유 코드 */
    private String quitReasonCode;

    /** 회원 탈퇴 내용 */
    private String quitContent;

    /** 재가입 가능 일자 */
    private LocalDateTime rejoinDt;

    /** 개인정보 파기 일자 */
    private LocalDateTime deleteDt;
}
