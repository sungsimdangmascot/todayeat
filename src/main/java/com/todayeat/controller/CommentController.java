package com.todayeat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todayeat.dto.CommentDTO;
import com.todayeat.dto.MemberDTO;
import com.todayeat.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
//	반환 : List<CommentDTO> -> JSON 배열[{...}, {...}, ...]
	@GetMapping("/comment/{postId}")
	public List<CommentDTO> findByPostId(@PathVariable("postId") int postId,
				HttpSession session){
//		(1) 댓글 목록 조회
		List<CommentDTO> comments = commentService.findByPostId(postId);
		
//		(2) 현재 로그인한 사용자의 memberId 확인
//		비로그인이면 0
		int loginMemberId = 0;
		if(session.getAttribute("loginMember") != null) {
			MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
			loginMemberId = loginMember.getMemberId();
		}
		
//		3) 각 댓글의 myComment 필드 설정
		for(CommentDTO comment : comments) {
			comment.setMyComment(comment.getMemberId() == loginMemberId);
		}
		
		return comments;
	}
	
//	@RequestBody - fetch가 보낸 JSON을 CommentDTO 객체로 자동 변환
	@PostMapping("/comment/write")
	public ResponseEntity<CommentDTO> write(@RequestBody CommentDTO commentDTO,
			HttpSession session){
//		(1) 로그인 확인
		if(session.getAttribute("loginMember") == null) {
//			로그인 인증 오류 응답 401 http 응답 코드 반환
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
//		(2) 세션에서 회원 정보를 가져와서 DTO에 설정
//		fetch가 보낸 JSON에서는 memberId, memberName이 없으므로 
//		서버에서 직접 채워줌
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		commentDTO.setMemberId(loginMember.getMemberId());
		
//		(3) DB에 댓글 저장
		CommentDTO saved = commentService.write(commentDTO);
		
//		(4) JS에 반환할 DTO에 이름과 본인 여부 추가
		saved.setMemberName(loginMember.getMemberName());
		saved.setMyComment(true);
		
//		Http 200응답과 데이터를 반환
		return ResponseEntity.ok(saved);
	}
	
//	댓글 삭제
	@PostMapping("/comment/delete/{commentId}")
//	반환 : {"success":true} JSON
	public ResponseEntity<Map<String, Boolean>> delete(
			@PathVariable("commentId") int commentId,
			HttpSession session){
//		(1) 로그인 확인
		if(session.getAttribute("loginMember") == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
//		(2) 댓글 삭제
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		commentService.delete(commentId, loginMember.getMemberId());
		
//		(3) 성공 응답 반환
		return ResponseEntity.ok(Map.of("success", true));
		
	}
	

}












