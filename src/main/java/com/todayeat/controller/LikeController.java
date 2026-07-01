package com.todayeat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todayeat.dto.LikeDTO;
import com.todayeat.dto.MemberDTO;
import com.todayeat.service.LikeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

// @RestController - @Controller + @ResponseBody 합친 것
@RestController
@RequiredArgsConstructor
public class LikeController {
	
	private final LikeService likeService;
	
//	1) 좋아요 토글 POST /like/{postId}
//	반환 타입 : ResponseEntity<LikeDTO>
//	ResponseEntity - HTTP 상태 코드(200, 401 등)와 응답 데이터를 함께  
//			함께 담는 클래스
//	- 정상처리 : HTTP 200 + {"liked":true, "likeCount" : 15}
//	- 비로그인 : HTTP 401 - js가 이 코드를 확인해 로그인 페이지로 이동
	
	@PostMapping("/like/{postId}")
	public ResponseEntity<LikeDTO> toggle(@PathVariable("postId") int postId,
					HttpSession session){
//		1) 로그인 확인
		if(session.getAttribute("loginMember") == null) {
//			HTTP 401 : 인증이 필요하다는 상태 코드
//			build() - 응답 본문없이 상태 코드만 반환
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
//		2) 좋아요 토글 처리
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		LikeDTO result = likeService.toggle(postId, loginMember.getMemberId());
	
//		3) HTTP 200 + LikeDTO Json 반환
//		ResponseEntity.ok() - HTTP 200 상태 코드와 데이터를 담아 반환
		return ResponseEntity.ok(result);
		
	}
	
	
	

}










