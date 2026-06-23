package com.todayeat.service;

import org.springframework.stereotype.Service;

import com.todayeat.dto.MemberDTO;
import com.todayeat.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor - final로 선언된 필드를 자동으로 주입해주는 
//			Lombok 어노테이션 memberMapper 필드가 spring에 의해 자동으로 연결
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	
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
	
	
}










