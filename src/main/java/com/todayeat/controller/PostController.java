package com.todayeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.PostDTO;
import com.todayeat.service.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
   
   private final PostService postService;
   
   
//   1) 작성 폼으로 이동
   @GetMapping("/post/write")
   public String writeForm(HttpSession session) {
//      세션에 loginMember가 없으면 => 로그인 페이지로 이동
      if(session.getAttribute("loginMember") == null) {
         return "redirect:/member/login-form";
      }
      
      return "post/write"; // templates/post/write.html
   }
   
//   2) 후기 글 작성
   @PostMapping("/post/write")
   public String write(PostDTO postDTO, HttpSession session) {
      
//      로그인 여부 확인
      if(session.getAttribute("loginMember") == null) {
         return "redirect:/member/login-form";
      }
      
//      세션에서 로그인 회원 정보 꺼내기(작성자 정보)
      MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
      
//      PostDTO에 작성자 번호(memberId) 설정
      postDTO.setMemberId(loginMember.getMemberId());
      
//      게시글 저장
      postService.write(postDTO);
      
//      저장 완료 후 메인 화면으로 이동
      return "redirect:/";
   }
   
//   3) 게시글 상세 화면 이동
   @GetMapping("/post/{postId}")
   public String detail(@PathVariable("postId") int postId, Model model) {
      
//      DB에서 게시글 1개 조회
      PostDTO post = postService.findById(postId);
      
      model.addAttribute("post", post);
      return "post/detail";
   }
   
   // 4) 게시글 수정 화면 이동
   @GetMapping("/post/update/{postId}")
   public String updateForm(@PathVariable("postId") int postId,
         Model model, HttpSession session) {
//      (1) 로그인 확인 -> 비로그인이면 로그인 페이지로 이동
      if(session.getAttribute("loginMember") == null) {
         return "redirect:/member/login-form";
      }
      
//      (2) 수정할 게시글 조회 
      PostDTO postDTO = postService.findById(postId);
      
//      (3) 권한 확인 - 로그인한 사람의 번호 vs 게시글 작성자 번호
      MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
      if(loginMember.getMemberId() != postDTO.getMemberId()) {
//         작성자가 아니면 수정 화면을 보여주지 않고 상세 페이지로 돌려보냄
         return "redirect:/post/"+postId;
      }
      
//      (4) 수정 폼에 기존 데이터를 채우기 위해 model에 담기
      model.addAttribute("post", postDTO);
      
      return "post/update";
   }
   
//   5) 게시글 수정 처리
   @PostMapping("/post/update/{postId}")
   public String update(@PathVariable("postId") int postId, PostDTO postDTO) {
      
//      (1) postDTO에 게시글 번호 설정
      postDTO.setPostId(postId);
      
//      (2) 게시글 수정
      postService.update(postDTO);
      
//      (3) 수정된 게시글 상세 페이지로 이동
      return "redirect:/post/" + postId;
      
   }
   

}








