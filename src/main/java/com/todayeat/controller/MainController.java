package com.todayeat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.todayeat.dto.PostDTO;
import com.todayeat.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
   
   private final PostService postService;
   
   @GetMapping("/")
   public String main(Model model) {
      
//      DB에서 전체 게시글 목록 조회
      List<PostDTO> postList = postService.findAll();
      
//      main.html에서 ${postList}로 꺼내서 사용
      model.addAttribute("postList", postList);
      
      return "main";
   }
   

}










