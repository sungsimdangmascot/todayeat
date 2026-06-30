package com.todayeat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.PageInfo;
import com.todayeat.service.AdminService;
import com.todayeat.service.CommentService;
import com.todayeat.service.MemberService;
import com.todayeat.service.PostService;

import lombok.RequiredArgsConstructor;

// 권한 체크는 AdminInterceptor가 담당 -> 여기 도달한 요청은 이미 ADMIN
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final MemberService memberService;
	private final PostService postService;
	private final CommentService commentService;
	private final AdminService adminService;
	
	// GET  /admin?keyword=xxx&page=1
	@GetMapping("")
	public String index(Model model,
			@RequestParam(name="keyword", defaultValue="") String keyword,
			@RequestParam(name="page", defaultValue = "1") int page) {
		
//		관리자 페이지 상단 통계 카드 4개
		model.addAttribute("totalMember", memberService.countActiveMembers());
		model.addAttribute("withdrawCount", memberService.countWithdrawMembers());
		model.addAttribute("totalPost", postService.countAllPosts());
		model.addAttribute("totalComment", commentService.countAllComments());
		
//		회원 목록 - 검색 + 페이징
		int pageSize = 10;  // 1페이지당 회원 10명씩
		int totalCount = memberService.countMembersForAdmin(keyword);
		PageInfo pageInfo = new PageInfo(page, totalCount, pageSize);
		List<MemberDTO> memberList = memberService.findMembersForAdmin(
				keyword, pageInfo.getOffset(), pageSize);
		model.addAttribute("memberList", memberList);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("keyword", keyword);
		
		
		return "admin/index";
	}
	
//	회원 강제 탈퇴 / 탈퇴신청 승인(둘다 회원 삭제)
	@PostMapping("/member/delete/{memberId}")
	public String deleteMember(@PathVariable(name="memberId") int memberId) {
		adminService.deleteMember(memberId);
		return "redirect:/admin";
	}
	
	
}













