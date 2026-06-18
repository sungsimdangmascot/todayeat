package com.todayeat.service;

import org.springframework.stereotype.Service;

import com.todayeat.dto.MemberDTO;
import com.todayeat.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor - final로 선언된 필드를 자동으로 주입해주는 
//         Lombok 어노테이션 memberMapper 필드가 spring에 의해 자동으로 연결
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
   
//  로그인 처리
   public MemberDTO login(MemberDTO memberDTO) {
//	   1) 아이디로 DB조회 
	   MemberDTO findMember = memberMapper.findByLoginId(memberDTO.getMemberLoginId());
	   
//	   아이디가 존재하지 않다면 null 반환 
	   if(findMember == null) {
		   return null;
	   }
	   if(!findMember.getMemberPwd().equals(memberDTO.getMemberPwd())) {
//		   비밀번호 불일치 -> null 반환(로그인 실패)
		   return null;
	   }
	   
	   return findMember;
	   
   }
   
   
}










