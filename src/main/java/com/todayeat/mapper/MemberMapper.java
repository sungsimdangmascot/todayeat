package com.todayeat.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.todayeat.dto.MemberDTO;

@Mapper
public interface MemberMapper {
//	회원 저장 
	void join(MemberDTO memberDTO);
	
//	아이디로 회원 조회
//	: 아이디 중복 확인에 사용 
	MemberDTO findByLoginId(String memberLoginId);
	
//	회원 번호로 회원 1명 조회 - 마이페이지 표시에 사용
//	로그인 시 저장한 세션의 memberId로 최신 회원 정보를 가져옴
	MemberDTO findById(int memberId);

//	회원 이름 수정
	void updateInfo(MemberDTO memberDTO);
	
//	비밀번호 변경
	void updatePwd(MemberDTO memberDTO);
	
//	회원탈퇴 신청
//	=> 실제 행을 삭제하지 않음
	void withdraw(int memberId);
	
//	필로필 사진만 별도로 수정
//	updateInfo와 분리한 이유 : 사진 업로드 여부와 이름 수정 여부가 서로 독립적이기 때문에
//	updateInfo에 MEMBER_PROFILE_IMG를 합치면 사진을 올리지 않을때 기존 사진이 지워질 수 있음
	void updateProfileImg(MemberDTO memberDTO);
	
}






