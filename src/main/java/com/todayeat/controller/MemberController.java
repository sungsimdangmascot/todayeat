package com.todayeat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.PostDTO;
import com.todayeat.service.MemberService;
import com.todayeat.service.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

//	MemberSerivce와 연결
	private final MemberService memberService;
//	PostService와 연결
	private final PostService postService;
	
//	파일 업로드 경로
	@Value("${todayeat.upload.path}")
	private String uploadPath;
	

//	1) 회원 가입 페이지로 이동
	@GetMapping("/join-form")
	public String joinForm() {
		return "member/join-form";
	}

//	2) 회원 가입 처리
	@PostMapping("/join")
	public String join(MemberDTO memberDTO, Model model) {
		// 1) 비밀번호 일치 확인
		// 첫번째 입력한 비밀번호와 두번째 입력한 비밀번호 일치 여부 확인
		if (!memberDTO.getMemberPwd().equals(memberDTO.getMemberPwdCheck())) {
			// 불일치 -> 오류 메시지를 화면에 전달하고 회원가입 폼으로 돌아감
			model.addAttribute("errorMsg", "비밀번호가 일치하지 않습니다.");
			return "member/join-form";
		}

		// 2) 회원가입 처리(아이디 중복 확인 포함)
		boolean result = memberService.join(memberDTO);

		// 3) 결과에 따라 처리
		if (!result) {
			// 아이디 중복 -> 오류 메시지를 화면에 전달하고 폼으로 돌아감
			model.addAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
			return "member/join-form";
		}

		// 4) 가입 성공 -> 로그인 화면으로 이동
		return "redirect:/member/login-form";
	}

//	3) 로그인 화면 이동
	@GetMapping("/login-form")
	public String loginForm() {
		return "member/login-form";
	}

//	4) 로그인 처리
	@PostMapping("/login")
	public String login(MemberDTO memberDTO, Model model, HttpSession session) {
//		(1) 로그인 처리(아이디 비밀번호 확인)
		MemberDTO loginMember = memberService.login(memberDTO);

//		(2) 로그인 실패 -> 오류 메시지와 함께 로그인 폼으로 되돌아감
		if (loginMember == null) {
			model.addAttribute("errorMsg", "아이디 또는 비밀번호가 틀렸습니다.");
			return "member/login-form";
		}

//		(3) 로그인 성공 -> 세션에 회원 정보 저장
		session.setAttribute("loginMember", loginMember);

//		(4) 메인 화면으로 이동
		return "redirect:/";
	}

//	5) 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {

//		세션 전체 삭제 - 저장된 모든 로그인 정보가 삭제
		session.invalidate();

//		메인 화면으로 이동
		return "redirect:/";
	}

//	6) 마이페이지 이동
	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
//		(1) 로그인 확인
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return "redirect:/member/login-form";
		}

//		(2) DB에서 회원 정보 조회
		MemberDTO member = memberService.findById(loginMember.getMemberId());

//		(3) 조회한 회원 정보를 HTML로 전달
		model.addAttribute("member", member);
		
//		(4) 내가 쓴 후기 목록 조회
		List<PostDTO> postList = postService.findByMemberId(loginMember.getMemberId());
		model.addAttribute("postList", postList);
		
		return "member/mypage";
	}

//	7) 회원 정보 수정  
//	회원 이름 수정 + 조건부 비밀번호 변경 처리
//	수정 폼 하나로 이름 수정과 비밀번호 변경을 함께 처리
//	- 현재 비밀번호 칸이 비어 있으면 -> 이름만 수정
//	- 현재 비밀번호 칸에 입력이 있으면 -> 이름 수정 + 비밀번호 변경 시도
	@PostMapping("/update")
	public String update(MemberDTO memberDTO, HttpSession session, 
			RedirectAttributes redicrectAttrs) throws IOException {
		// +) RedirectAttributes
//		=> 리다이렉트를 실행할때,  컨트롤러에서 다른 컨트롤러에 
//		Attribute를 전달하는데 이용

//		(1) 로그인 확인
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if (loginMember == null) {
			return "redirect:/member/login-form";
		}
//		(2) 세션의 memberId를 memberDTO에 설정
		memberDTO.setMemberId(loginMember.getMemberId());

//		(3) 이름 수정
		memberService.updateInfo(memberDTO);
		
//		--- 프로필 사진이 선택된 경우에만 저장 처리
		if(memberDTO.getMemberProfileImgFile() != null 
				&& !memberDTO.getMemberProfileImgFile().isEmpty()) {
//			파일 저장
			String savedFileName = saveFile(memberDTO.getMemberProfileImgFile());
			
//			memberDTO에 새 파일명 세팅 후 DB 업데이트
			memberDTO.setMemberProfileImg(savedFileName);
			memberService.updateProfileImg(memberDTO);
		}

//		(4) 세션 갱신 
		loginMember.setMemberName(memberDTO.getMemberName());
		session.setAttribute("loginMember", loginMember);

//		(5) 현재 비밀번호가 입력된 경우에만 비밀번호 변경 시도
//		isEmpty() : 문자열이 빈 값("")인지 확인
//		현재 비밀번호 칸을 비워두면 이 블록 자체를 건너뜀
		if (memberDTO.getCurrentPwd() != null && !memberDTO.getCurrentPwd().isEmpty()) {
			boolean pwdResult = memberService.changePwd(loginMember.getMemberId(), // 본인 회원 번호
					memberDTO.getCurrentPwd(), // 폼에서 입력한 현재 비밀번호
					memberDTO.getMemberPwd() // 폼에서 입력한 새 비밀번호
			);
			
//			현재 비밀번호 불일치 -> 오류 메시지와 함께 마이페이지로 이동
			if(!pwdResult) {
				redicrectAttrs.addFlashAttribute("errorMsg", 
						"현재 비밀번호가 일치하지 않습니다.");
				return "redirect:/member/mypage";
			}
			
//			현재 비밀번호 일치 -> 비밀번호 변경 성공 메시지 
			redicrectAttrs.addFlashAttribute("successMsg", 
					"비밀번호가 변경되었습니다.");
		} else {
//			6) 이름만 수정한 경우 -> 성공 메시지와 마이페이지로 이동
			redicrectAttrs.addFlashAttribute("successMsg", 
					"회원정보가 수정되었습니다.");
		}
		return "redirect:/member/mypage";
	}
	
//	회원 탈퇴 신청
	@PostMapping("/delete")
	public String withdraw(HttpSession session) {
		
//		(1) 로그인 확인
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/member/login-form";
		}
		
//		(2) DB에서 MEMBER_STATUS = 'WITHDRAW'로 변경
		memberService.withdraw(loginMember.getMemberId());
		
//		(3) 세션에서 삭제 => 로그인 상태 해제
//		탈퇴했는데 세션이 남아 로그인 상태로 계속 사용하는 상황 방지
		session.invalidate();
		
//		(4) 메인 화면으로 이동(비로그인 상태)
		return "redirect:/";
	}
	
//	-------- 파일 저장 메서드 --------------
//	반환값 : DB에 저장할 새 파일명(UUID기반)
	private String saveFile(MultipartFile file) throws IOException {
//		(1) 원본 파일명에서 확장자 추출
//		getOriginalFilename() : 사용자 PC에서 원본 파일명을 반환
//					ex) "pizza.jpg"
//		lastIndexOf(".") : 마지막 점(.) 위치 찾기
//		substring(점위치) : 점 포함 이후 문자열 추출 -> ".jpg"
		String originalName = file.getOriginalFilename();
		String ext = originalName.substring(originalName.lastIndexOf("."));
//		ex) "피자.jpg" => ext = ".jpg"
		
//		(2) UUID로 고유한 새 파일명 생성
//		randomUUID() : 겹치지 않는 고유 ID생성
//		toString() - "5550e8400-e49b..." 형태의 문자열로 반환
		String savedName = UUID.randomUUID().toString() + ext;
		
//		(3) 업로드 폴더가 없으면 자동으로 생성
//		new File(uploadPath) - "C:/upload/todayeat/" 폴더를 가리키는 객체
		File uploadDir = new File(uploadPath);
		if(!uploadDir.exists()) {
//			폴더가 없다면 
			uploadDir.mkdirs(); 
//			mkdirs() : 폴더가 없으면 생성
		}
		
//		(4) 파일 실제 저장
//		Paths.get(uploadPath+savedName) - 저장할 전체 경로 생성
//		ex) C:/upload/todayeat/550....jpg
		Path savePath = Paths.get(uploadPath+savedName);
		
//		Files.copy(파일데이터, 저장경로)
//		getInputStream() : MultipartFile에서 실제 파일 데이터를 꺼냄 
		Files.copy(file.getInputStream(), savePath);
		
//		(5) DB에 저장할 파일명 반환(전체 경로가 아닌 이름만)
//		나중에 <img src="/upload/파일명"> 형태로 사용
		return savedName;
	}

}









