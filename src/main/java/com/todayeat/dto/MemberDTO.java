package com.todayeat.dto;

import org.springframework.web.multipart.MultipartFile;

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
	
//	DB에 없는 필드 - 비밀번호 확인용으로만 사용
//	사용자가 비밀번호를 두번 입력했을때 두번째 값을 받는 임시 보관함
	private String memberPwdCheck;
	
//	마이 페이지의 현재 비밀번호 확인용 
	private String currentPwd;
	
//	폼에서 업로드한 프로필 사진 파일을 받는 필드
//	- memberProfileImg  : DB 저장용(String, 파일명)
//	- memberProfileImgFile - 폼 수신용(실제 파일)
//	둘을 분리하는 이유 : DB 컬럼과 폼 입력 타입이 다르기 때문에
	private MultipartFile memberProfileImgFile;
	
}




