package com.todayeat.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.todayeat.dto.MemberDTO;

@Mapper
public interface MemberMapper {
//   회원 저장 
   void join(MemberDTO memberDTO);
   
//   아이디로 회원 조회
//   : 아이디 중복 확인에 사용 
   MemberDTO findByLoginId(String memberLoginId);
   
}
