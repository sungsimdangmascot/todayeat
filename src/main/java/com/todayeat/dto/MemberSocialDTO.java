package com.todayeat.dto;

import lombok.Data;

@Data
public class MemberSocialDTO {
	
	private int socialId; 
	private int memberId; // 우리 회원 번호
	private String socialType; // 소셜 종류 - "KAKAO" 또는 "GOOGLE"
	private String socialKey; // 카카오에서 발급한 회원 고유 번호

}
