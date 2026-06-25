package com.todayeat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todayeat.dto.PageInfo;
import com.todayeat.dto.PostDTO;
import com.todayeat.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
   
   private final PostService postService;
   
//   kakao.map.key 값
   @Value("${kakao.map.key}")
   private String kakaoKey;
   
//   @RequestParam : URL 쿼리 파라미터 (?keyword=...&page=...)를 메서드 매개변수로 받음
//   name  - URL 파라미터 이름을 명시
   
//   defaultValue : URL에 해당 파라미터가 없을때 사용할 기본값
//    keyword 기본값 : "" -> 파라미터 없으면 빈 문자열(검색 없음, 전체 조회)
//    page 기본값 : "1"  -> 파라미터 없으면 1페이지
   @GetMapping("/")
   public String main(Model model,
         @RequestParam(name="keyword", defaultValue = "") String keyword,
         @RequestParam(name="page", defaultValue = "1") int page) {
      
//      현재 페이지에서 보여줄 게시글 수
      int pageSize = 6;
      
//      DB에서 검색 조건에 맞는 게시글 총 개수 조회
//         검색어가 없으면 전체 게시글 수를 반환
      int totalCount = postService.countAll(keyword);
      
//      PageInfo 생성 - 총 개수와 현재 페이지를 넘기면 모든 계산이 자동으로 처리됨
      PageInfo pageInfo = new PageInfo(page, totalCount, pageSize);
      
//      DB에서 전체 게시글 목록 조회
      List<PostDTO> postList = postService.findAll(keyword, pageInfo.getOffset(), pageSize);
      
//      main.html에서 ${postList}로 꺼내서 사용
      model.addAttribute("postList", postList);
      model.addAttribute("keyword", keyword); // 검색어(검색 칸 유지에 사용)
      model.addAttribute("pageInfo", pageInfo); // 페이지 정보
      
      return "main";
   }
   
//   map 지도 화면으로 이동 
   @GetMapping("/map")
   public String map(Model model) {
      model.addAttribute("kakaoKey", kakaoKey);
      return "map";
   }
   
//   @ResponseBody - 화면(HTML) 대신 JSON 데이터를 직접 반환하는 어노테이션
//   이 메서드 하나만 JSON을 반환함(@RestController와 달리 클래스 전체 적용이 아님)
//   map.js 의 fetch('/api/map/posts') 요청에 응답
   @GetMapping("/api/map/posts")
   @ResponseBody
   public List<PostDTO> mapPosts(){
      return postService.findAllForMap();
   }
   
   

}










