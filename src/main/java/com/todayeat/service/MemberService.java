package com.todayeat.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.MemberSocialDTO;
import com.todayeat.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor - final로 선언된 필드를 자동으로 주입해주는 
//			Lombok 어노테이션 memberMapper 필드가 spring에 의해 자동으로 연결
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	// kakaoService 주입 - 카카오API 호출담당
	private final KakaoService kakaoService;
	// googleService 주입 - 구글API 호출담당
	private final GoogleService googleService;  
	
	// 회원가입 처리
	// 반환값 : true -> 가입 성공 / false -> 아이디 중복
	public boolean join(MemberDTO memberDTO) {
		
		// 1) 아이디 중복 확인 - DB에서 동일한 아이디 조회
		MemberDTO existMember = memberMapper.findByLoginId(memberDTO.getMemberLoginId());
		
		// 2) 이미 같은 아이디가 있으면 false 반환(가입 중단)
		if(existMember != null) {
			return false;
		}
		// 3) 중복이 없으면 DB에 저장
		memberMapper.join(memberDTO);
		
		// 4) 저장 성공 - true 반환
		return true;
	}
	
//	로그인 처리
//  반환값 : 로그인 성공 -> MemberDTO(회원정보) / 실패 -> null
	public MemberDTO login(MemberDTO memberDTO) {
//		1) 아이디로 DB 조회
		MemberDTO findMember = memberMapper.findByLoginId(memberDTO.getMemberLoginId());
		
//		2) 아이디가 존재하지 않으면 null 반환(로그인 실패)
		if(findMember == null) {
			return null;
		}
//		+) 탈퇴를 신청한 회원이면 로그인 거부
		if(!"ACTIVE".equals(findMember.getMemberStatus())) {
			// 탈퇴 신청을 한 경우
			return null; // null 반환 -> 
//				Controller에서 "아이디 또는 비밀번호가 틀렸습니다" 메시지 출력
		}
		
//		3) 비밀번호 비교 - 입력한 비밀번호 vs DB에 저장된 비밀번호
		if(!findMember.getMemberPwd().equals(memberDTO.getMemberPwd())) {
//			비밀번호 불일치 -> null 반환(로그인 실패)
			return null;
		}
		
//		4) 아이디, 비밀번호 모두 일치 -> 회원 정보 반환(로그인 성공)
		return findMember;
		
	}
	
//	회원 조회 - 마이페이지 표시용
	public MemberDTO findById(int memberId) {
		return memberMapper.findById(memberId);
	}
	
//	회원 이름 수정 
//	Controller에서 memberId를 세션 값으로 주입한 뒤 전달받음
	public void updateInfo(MemberDTO memberDTO) {
		memberMapper.updateInfo(memberDTO);
	}
	
//	비밀번호 변경
//	반환값 : true = 변경 성공 / false = 현재 비밀번호 불일치
	public boolean changePwd(int memberId, String currentPwd, 
			String newPwd) {
		
//		(1) DB에서 현재 저장된 비밀번호 가져오기
		MemberDTO findMember = memberMapper.findById(memberId);
		
//		(2) 사용자가 입력한 currentPwd와 DB의 비밀번호 비교
		if(!findMember.getMemberPwd().equals(currentPwd)) {
			return false;   // 불일치 -> 변경 중단
		}
		
//		(3) 새 비밀번호로 update
//		UPDATE에 필요한 값(memberId, memberPwd)만 담은 새 DTO 생성
		MemberDTO updateDTO = new MemberDTO();
		updateDTO.setMemberId(memberId);
		updateDTO.setMemberPwd(newPwd);
		memberMapper.updatePwd(updateDTO);
		
		return true; // 변경 성공
	}
	
//	회원 탈퇴 신청
	public void withdraw(int memberId) {
		memberMapper.withdraw(memberId);
	}
	
//	프로필 사진 경로 수정
	public void updateProfileImg(MemberDTO memberDTO) {
		memberMapper.updateProfileImg(memberDTO);
	}
	
//	카카오 소셜 로그인 처리
//	파라미터: code - 카카오가 전달한 인가코드(URL의 ?code=xxx)
//	반환값 : 로그인된 회원 정보(MemberDTO)
	@SuppressWarnings("unchecked") // Map캐스팅 경고 무시
	public MemberDTO kakaoLogin(String code) {
		
//		(1) 인가코드 -> 액세스 토큰 교환
//		카카오 서버에 인가코드를 보내고, 액세스 토큰을 받아옴
		String accessToken = kakaoService.getAccessToken(code);
		
//		(2) 액세스 토큰 -> 카카오 사용자 정보 조회
//		카카오 서버에 토큰을 보내고, 회원 정보를 받아옴
		Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
		
//		(3) 사용자 정보에서 필요한 값 꺼내기
//		카카오 회원번호 - 카카오가 각 회원에게 부여하는 고유 숫자
//		=> 우리 서버의 social_key
		String socialKey = String.valueOf(userInfo.get("id"));
		
//		kakao_account 안에 이메일, 닉네임이 있음
//		동의하지 않은 항목은 null이기 때문에 기본값 설정
		String nickname = "카카오회원";
		String email = ""; // 이메일 기본값
		
		Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
		
		if(kakaoAccount != null) {
			// 프로필 정보(닉네임)
			Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
			
			if(profile != null && profile.get("profile_nickname") != null) {
				nickname = (String)profile.get("profile_nickname");
			}
		}
		
//		4) MEMER_SOCIAL 조회 - 이미 카카오로 가입한 회원인지 확인
//		처음 로그인이면 null 반환, 재방문이면 기존 회원 정보 반환
		MemberDTO existMember = memberMapper.findBySocial("KAKAO", socialKey);
		if(existMember != null) {
			// 기존 회원 -> 바로 반환(세션에 저장할 정보)
			return existMember;
		}
		
//		5) 신규회원 - MEMBER테이블에 자동 기입
//		카카오 로그인 회원은 우리 아이디/비밀번호 없이 자동으로 계정 생성
		MemberDTO newMember = new MemberDTO();
		newMember.setMemberLoginId("kakao_"+socialKey); // 아아디 : kakao_카카오번호
		newMember.setMemberName(nickname);
		newMember.setMemberEmail(email);
		newMember.setMemberPwd("KAKAO_SOCIAL");
		
		memberMapper.join(newMember); // MEMBER테이블에 저장
		
//		6) MEMBER_SOCIAL 테이블에 연동 정보 저장
//		방금 저장한 회원의 MEMBER_ID를 가져오기 위해 다시 조회
		MemberDTO savedMember = memberMapper.findByLoginId("kakao_"+socialKey);
		
		MemberSocialDTO socialDTO = new MemberSocialDTO();
		socialDTO.setMemberId(savedMember.getMemberId()); // 우리 회원번호
		socialDTO.setSocialType("KAKAO");
		socialDTO.setSocialKey(socialKey); // 카카오 회원 번호
		
		memberMapper.saveSocial(socialDTO); // MEMBER_SOCIAL 테이블에 저장
		
//		새로 가입한 회원 정보 반환(세션에 저장할 번호)
		return savedMember;
		
	}
	
//	구글 소셜 로그인 
//	파라미터 : code - 구글이 전달한 인가코드
//	반환값 : 로그인된 회원 정보(MemberDTO)
	public MemberDTO googleLogin(String code) {
		
//	 	(1) 인가코드 -> 액세스 토큰 교환
		String accessToken = googleService.getAccessToken(code);
		
//		(2) 액세스 토큰 -> 구글 사용자 정보 조회
		Map<String, Object> userInfo = googleService.getUserInfo(accessToken);
		
//		(3) 사용자 정보 추출
//		구글 회원 고유번호
		String socialKey = String.valueOf(userInfo.get("id"));
		
//		이름, 이메일
		String nickname = "구글회원"; // 기본값
		String email = ""; 
		
		if(userInfo.get("name") != null) {
			nickname = (String) userInfo.get("name");
		}
		if(userInfo.get("email") != null) {
			email = (String) userInfo.get("email");
		}
		
//		(4) 기존 소셜 회원 확인 
		MemberDTO existMember = memberMapper.findBySocial("GOOGLE", socialKey);
		if(existMember != null) {
			return existMember;
		}
		
//		(5) 신규회원 가입 - MEMEBR테이블에 저장
		MemberDTO newMember = new MemberDTO();
		newMember.setMemberLoginId("google_"+socialKey); //아이디 : google_구글번호
		newMember.setMemberName(nickname);
		newMember.setMemberEmail(email);
		newMember.setMemberPwd("GOOGLE_SOCIAL");
		
		memberMapper.join(newMember);
		
		// (6) MEMBER_SOCIAL 연동 정보 저장
		
		// 위에서 MEMBER테이블에 추가한 회원 정보 가져오기
		MemberDTO savedMember = memberMapper.findByLoginId("google_"+socialKey);
		
		MemberSocialDTO socialDTO = new MemberSocialDTO();
		socialDTO.setMemberId(savedMember.getMemberId());
		socialDTO.setSocialType("GOOGLE");
		socialDTO.setSocialKey(socialKey);
		
		memberMapper.saveSocial(socialDTO);
		
		return savedMember;
	}
	
//	관리자 통계 - 정상(ACTIVE) 회원 수
	public int countActiveMembers() {
		return memberMapper.countActiveMembers();
	}
	
//	관리자 통계 - 탈퇴 신청(WITHDRAW) 회원수
	public int countWithdrawMembers() {
		return memberMapper.countWithdrawMembers();
	}
	
//	관리자 회원 목록 
	public List<MemberDTO> findMembersForAdmin(String keyword,
			int offset, int size){
		return memberMapper.findMembersForAdmin(keyword, offset, size);
	}
	
//	관리자 회원 목록 - 검색어 포함 전체 건수(페이징 계산용)
	public int countMembersForAdmin(String keyword) {
		return memberMapper.countMembersForAdmin(keyword);
	}
	
//	탈퇴 신청 회원 목록
	public List<MemberDTO> findWithdrawMembers(){
		return memberMapper.findWithdrawMembers();
	}
	
	
}










