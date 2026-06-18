package com.todayeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todayeat.dto.MemberDTO;
import com.todayeat.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
   
//   MemberSerivce와 연결
   private final MemberService memberService;
   
//   1) 회원 가입 페이지로 이동
   @GetMapping("/join-form")
   public String joinForm() {
      return "member/join-form";
   }
   
//   2) 회원 가입 처리
   @PostMapping("/join")
   public String join(MemberDTO memberDTO, Model model) {
      // 1) 비밀번호 일치 확인
      // 첫번째 입력한 비밀번호와 두번째 입력한 비밀번호 일치 여부 확인
      if(!memberDTO.getMemberPwd().equals(memberDTO.getMemberPwdCheck())){
         // 불일치 -> 오류 메시지를 화면에 전달하고 회원가입 폼으로 돌아감
         model.addAttribute("errorMsg", "비밀번호가 일치하지 않습니다.");
         return "member/join-form";
      }
      
      // 2) 회원가입 처리(아이디 중복 확인 포함)
      boolean result = memberService.join(memberDTO);
      
      // 3) 결과에 따라 처리
      if(!result) {
         // 아이디 중복 -> 오류 메시지를 화면에 전달하고 폼으로 돌아감
         model.addAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
         return "member/join-form";
      }
      
      // 4) 가입 성공 -> 로그인 화면으로 이동
      return "redirect:/member/login-form";
   }
   
//   3) 로그인 화면 이동
   @GetMapping("/login-form")
   public String loginForm() {
      return "member/login-form";
   }
   
   @PostMapping("/login")
   public String login(MemberDTO memberDTO, Model model, HttpSession session) {
//	   (1) 로그인 처리 (아이디 비밀번호 확인)
	   MemberDTO loginMember = memberService.login(memberDTO);
	   
	   if(loginMember == null) {
		   model.addAttribute("errorMsg", "아이디 또는 비밀번호가 틀렸습니다");
		   return "member/login-form";
	   }
	   session.setAttribute("loginMember", loginMember);
	   
	   return "redirect:/"; 
   }
   
   // 로그아웃 처리
   @GetMapping("/logout")
   public String logout(HttpSession session) {
	   // 세션 전체 삭제 - 저장된 모든 로그인 정보가 삭제
	   session.invalidate();
	   // 메인 화면으로 이동
	   return "redirect:/";
   }

}

