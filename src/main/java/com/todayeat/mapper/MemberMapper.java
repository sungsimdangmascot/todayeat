package com.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.MemberSocialDTO;

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
	
//	소셜 타입 + 소셜 키로 연동된 회원 조회
//	카카오 로그인 "이미 가입한 회원인지"확인할때 사용
	MemberDTO findBySocial(@Param("socialType") String socialType,
						   @Param("socialKey") String socialKey);
	
//	MEMBER_SOCIAL 테이블에 소셜 연동 정보 저장
//	카카오 첫 로그인 시 (신규 회원 자동 가입 후) 실행
	void saveSocial(MemberSocialDTO socialDTO);
	
//	관리자 통계 - 전체 활성 회원 수(탈퇴 제외)
	int countActiveMembers();
	
//	관리자 통계 - 탈퇴 신청(WITHDRAW) 회원 수
	int countWithdrawMembers();
	
//	관리자 회원 목록
	List<MemberDTO> findMembersForAdmin(@Param("keyword") String keyword,
			@Param("offset") int offset,
			@Param("size") int size);
	
// 관리자 회원 목록 - 검색어 포함 검색 건수(페이징 계산)
	int countMembersForAdmin(@Param("keyword") String keyword);
	
//	연쇄 삭제용-이 회원의 소셜 로그인 연결(MEMBER_SOCIAL) 삭제
	void deleteSocialByMember(int memberId);
	
//	연쇄 삭제용-회원 본인 삭제
	void deleteById(int memberId);
	
	
}






