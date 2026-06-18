package com.todayeat.dto;

import lombok.Data;

// @Data - Lombok이 getter, setter, toString, equals를 자동으로 만들어줌  
@Data   
public class MemberDTO {
   // MyBatis가 member_id -> memberId로 자동 변환
   private int memberId;
   private String memberLoginId;
   private String memberPwd;
   private String memberName;
   private String memberEmail;
   private String memberProfileImg;
   private String memberRole; // 기본값 USER
   private String memberStatus; // 기본값 active
   private String memberCreatedAt; 
   
//   DB에 없는 필드 - 비밀번호 확인용으로만 사용
//   사용자가 비밀번호를 두번 입력했을때 두번째 값을 받는 임시 보관함
   private String memberPwdCheck;
   

}




