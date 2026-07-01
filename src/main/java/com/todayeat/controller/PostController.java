<<<<<<< HEAD
package com.todayeat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.PostDTO;
import com.todayeat.service.LikeService;
import com.todayeat.service.PlaceService;
import com.todayeat.service.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	private final PostService postService;
	private final LikeService likeService;
	private final PlaceService placeService;
	
//	application.properties의 today.upload.path값을 가져옴
	@Value("${todayeat.upload.path}")
	private String uploadPath;
	
//	kakao.map.key 값
	@Value("${kakao.map.key}")
	private String kakaoKey;
	
	
//	1) 작성 폼으로 이동
	@GetMapping("/post/write")
	public String writeForm(HttpSession session, Model model) {
//		세션에 loginMember가 없으면 => 로그인 페이지로 이동
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
//		---- 카카오 맵을 로드할때 필요한 kakaoKey
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/write"; // templates/post/write.html
	}
	
//	2) 후기 글 작성
//	- throws IOException - 파일 저장 중 문제(예외)가 발생하면 예외를 던짐
//				(spring이 에러 페이지로 처리)
	@PostMapping("/post/write")
	public String write(PostDTO postDTO, HttpSession session) 
			throws IOException{
		
//		로그인 여부 확인
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
		
//		+) --- 장소 필수 검증 -----
//		장소 없이 POST INSERT하면 PLACE_ID NOT NULL 제약 조건 위반으로
//		DB오류 발생
		if(postDTO.getKakaoPlaceId() == null || postDTO.getKakaoPlaceId().isEmpty()) {
			// 작성 폼으로 돌려보냄
			return "redirect:/post/write";
		}
		
//		세션에서 로그인 회원 정보 꺼내기(작성자 정보)
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		
//		PostDTO에 작성자 번호(memberId) 설정
		postDTO.setMemberId(loginMember.getMemberId());
		
//		--- 장소 저장 ------------
//		placeService.getOrSave : 이미 있는 장소면 그 placed_id 반환,
//		없으면 insert 후 새 place_id 반환
		int placeId = placeService.getOrSave(postDTO);
		postDTO.setPlaceId(placeId);
		
//		----- 이미지 파일 저장 처리 -----------
//		isEmpty() : 사용자가 파일을 선택하지 않고 "등록하기"를 누르면 true
//					파일을 선택했으면 false
		if(postDTO.getImgFile() != null && !postDTO.getImgFile().isEmpty()) {
//				파일 저장 후 DB에 저장할 파일명을 postDTO에 세팅
				String savedFileName = saveFile(postDTO.getImgFile());
				postDTO.setPostImg(savedFileName);
		}
		
//		게시글 저장
		postService.write(postDTO);
		
//		저장 완료 후 메인 화면으로 이동
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
	
	
//	3) 게시글 상세 화면 이동
	@GetMapping("/post/{postId}")
	public String detail(@PathVariable("postId") int postId, Model model,
				HttpSession session) {
		
//		DB에서 게시글 1개 조회
		PostDTO post = postService.findById(postId);
		model.addAttribute("post", post);
		
//		좋아요 수 조회 - 로그인 여부와 상관없이 항상 표시
		int likeCount = likeService.countByPostId(postId);
		model.addAttribute("likeCount", likeCount);
		
//		현재 사용자의 좋아요 여부 - 로그인한 경우에만 확인
//		비로그인이면 false (좋아요 버튼이 비활성 색으로 표시)
		boolean liked = false;
		if(session.getAttribute("loginMember") != null) {
			MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
			liked = likeService.isLiked(postId, loginMember.getMemberId());
		}
		model.addAttribute("liked", liked);
		
//		detail.html에서 카카오맵 SDK를 로드할때 kakaokey필요
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/detail";
	}
	
	// 4) 게시글 수정 화면 이동
	@GetMapping("/post/update/{postId}")
	public String updateForm(@PathVariable("postId") int postId,
			Model model, HttpSession session) {
//		(1) 로그인 확인 -> 비로그인이면 로그인 페이지로 이동
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
		
//		(2) 수정할 게시글 조회 
		PostDTO postDTO = postService.findById(postId);
		
//		(3) 권한 확인 - 로그인한 사람의 번호 vs 게시글 작성자 번호
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if(loginMember.getMemberId() != postDTO.getMemberId()) {
//			작성자가 아니면 수정 화면을 보여주지 않고 상세 페이지로 돌려보냄
			return "redirect:/post/"+postId;
		}
		
//		(4) 수정 폼에 기존 데이터를 채우기 위해 model에 담기
		model.addAttribute("post", postDTO);
		
		// update.html에서 카카오맵 SDK를 로드할때 kakaokey필됴
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/update";
	}
	
//	5) 게시글 수정 처리
	@PostMapping("/post/update/{postId}")
	public String update(@PathVariable("postId") int postId, PostDTO postDTO)
		throws IOException{
		
//		(1) postDTO에 게시글 번호 설정
		postDTO.setPostId(postId);
		
//		---- 장소 변경 반영 ---------------
//		수정 폼에서 위치를 바꾸면 새 카카오 장소 정보가 hidden 필드로 전송됨
//		getOrSave : 이미 있는 장소면 그 place_id 반환, 처음 나온 장소면 INSERT 
//		새 place_id 반환(위치를 바꾸지 않았다면 기존 장소가 그대로 조회되어 place_id가 조회됨)
		if(postDTO.getKakaoPlaceId() != null && !postDTO.getKakaoPlaceId().isEmpty()) {
			int placeId = placeService.getOrSave(postDTO);
			postDTO.setPlaceId(placeId);
		}
		
//		+) 새로운 이미지가 선택된 경우에만 파일 저장 후 postImg 수정
		if(postDTO.getImgFile() != null && !postDTO.getImgFile().isEmpty()) {
			String savedFileName = saveFile(postDTO.getImgFile());
			postDTO.setPostImg(savedFileName);
		}
		
//		(2) 게시글 수정
		postService.update(postDTO);
		
//		(3) 수정된 게시글 상세 페이지로 이동
		return "redirect:/post/" + postId;
	}
	
//	6) 게시글 삭제
	@PostMapping("/post/delete/{postId}")
	public String delete(@PathVariable("postId") int postId, 
				HttpSession session) {
//		(1) 로그인 확인
		if(session.getAttribute("loginMember") == null) {
//			비로그인 => 로그인 화면으로 이동
			return "redirect:/member/login-form";
		}
		
//		(2) 권한 확인
		PostDTO existPost = postService.findById(postId);
//		삭제하려는 게시글에 대한 정보 가져오기
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
//		로그인 회원 정보 가져오기
		
//		작성자 회원Id vs 로그인 회원 Id
		if(loginMember.getMemberId() != existPost.getMemberId()) {
			return "redirect:/post/"+postId;
		}
		
//		(3) 게시글 삭제
		postService.delete(postId);
		
//		(4) 삭제 완료 후 메인 화면으로 이동
		return "redirect:/";
		
		
	}
	
	

}








=======
package com.todayeat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.todayeat.dto.MemberDTO;
import com.todayeat.dto.PostDTO;
import com.todayeat.service.LikeService;
import com.todayeat.service.PlaceService;
import com.todayeat.service.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	private final PostService postService;
	private final LikeService likeService;
	private final PlaceService placeService;
	
//	application.properties의 today.upload.path값을 가져옴
	@Value("${todayeat.upload.path}")
	private String uploadPath;
	
//	kakao.map.key 값
	@Value("${kakao.map.key}")
	private String kakaoKey;
	
	
//	1) 작성 폼으로 이동
	@GetMapping("/post/write")
	public String writeForm(HttpSession session, Model model) {
//		세션에 loginMember가 없으면 => 로그인 페이지로 이동
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
//		---- 카카오 맵을 로드할때 필요한 kakaoKey
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/write"; // templates/post/write.html
	}
	
//	2) 후기 글 작성
//	- throws IOException - 파일 저장 중 문제(예외)가 발생하면 예외를 던짐
//				(spring이 에러 페이지로 처리)
	@PostMapping("/post/write")
	public String write(PostDTO postDTO, HttpSession session) 
			throws IOException{
		
//		로그인 여부 확인
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
		
//		+) --- 장소 필수 검증 -----
//		장소 없이 POST INSERT하면 PLACE_ID NOT NULL 제약 조건 위반으로
//		DB오류 발생
		if(postDTO.getKakaoPlaceId() == null || postDTO.getKakaoPlaceId().isEmpty()) {
			// 작성 폼으로 돌려보냄
			return "redirect:/post/write";
		}
		
//		세션에서 로그인 회원 정보 꺼내기(작성자 정보)
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
		
//		PostDTO에 작성자 번호(memberId) 설정
		postDTO.setMemberId(loginMember.getMemberId());
		
//		--- 장소 저장 ------------
//		placeService.getOrSave : 이미 있는 장소면 그 placed_id 반환,
//		없으면 insert 후 새 place_id 반환
		int placeId = placeService.getOrSave(postDTO);
		postDTO.setPlaceId(placeId);
		
//		----- 이미지 파일 저장 처리 -----------
//		isEmpty() : 사용자가 파일을 선택하지 않고 "등록하기"를 누르면 true
//					파일을 선택했으면 false
		if(postDTO.getImgFile() != null && !postDTO.getImgFile().isEmpty()) {
//				파일 저장 후 DB에 저장할 파일명을 postDTO에 세팅
				String savedFileName = saveFile(postDTO.getImgFile());
				postDTO.setPostImg(savedFileName);
		}
		
//		게시글 저장
		postService.write(postDTO);
		
//		저장 완료 후 메인 화면으로 이동
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
	
	
//	3) 게시글 상세 화면 이동
	@GetMapping("/post/{postId}")
	public String detail(@PathVariable("postId") int postId, Model model,
				HttpSession session) {
		
//		DB에서 게시글 1개 조회
		PostDTO post = postService.findById(postId);
		model.addAttribute("post", post);
		
//		좋아요 수 조회 - 로그인 여부와 상관없이 항상 표시
		int likeCount = likeService.countByPostId(postId);
		model.addAttribute("likeCount", likeCount);
		
//		현재 사용자의 좋아요 여부 - 로그인한 경우에만 확인
//		비로그인이면 false (좋아요 버튼이 비활성 색으로 표시)
		boolean liked = false;
		if(session.getAttribute("loginMember") != null) {
			MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
			liked = likeService.isLiked(postId, loginMember.getMemberId());
		}
		model.addAttribute("liked", liked);
		
//		detail.html에서 카카오맵 SDK를 로드할때 kakaokey필요
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/detail";
	}
	
	// 4) 게시글 수정 화면 이동
	@GetMapping("/post/update/{postId}")
	public String updateForm(@PathVariable("postId") int postId,
			Model model, HttpSession session) {
//		(1) 로그인 확인 -> 비로그인이면 로그인 페이지로 이동
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login-form";
		}
		
//		(2) 수정할 게시글 조회 
		PostDTO postDTO = postService.findById(postId);
		
//		(3) 권한 확인 - 로그인한 사람의 번호 vs 게시글 작성자 번호
		MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
		if(loginMember.getMemberId() != postDTO.getMemberId()) {
//			작성자가 아니면 수정 화면을 보여주지 않고 상세 페이지로 돌려보냄
			return "redirect:/post/"+postId;
		}
		
//		(4) 수정 폼에 기존 데이터를 채우기 위해 model에 담기
		model.addAttribute("post", postDTO);
		
		// update.html에서 카카오맵 SDK를 로드할때 kakaokey필됴
		model.addAttribute("kakaoKey", kakaoKey);
		
		return "post/update";
	}
	
//	5) 게시글 수정 처리
	@PostMapping("/post/update/{postId}")
	public String update(@PathVariable("postId") int postId, PostDTO postDTO)
		throws IOException{
		
//		(1) postDTO에 게시글 번호 설정
		postDTO.setPostId(postId);
		
//		---- 장소 변경 반영 ---------------
//		수정 폼에서 위치를 바꾸면 새 카카오 장소 정보가 hidden 필드로 전송됨
//		getOrSave : 이미 있는 장소면 그 place_id 반환, 처음 나온 장소면 INSERT 
//		새 place_id 반환(위치를 바꾸지 않았다면 기존 장소가 그대로 조회되어 place_id가 조회됨)
		if(postDTO.getKakaoPlaceId() != null && !postDTO.getKakaoPlaceId().isEmpty()) {
			int placeId = placeService.getOrSave(postDTO);
			postDTO.setPlaceId(placeId);
		}
		
//		+) 새로운 이미지가 선택된 경우에만 파일 저장 후 postImg 수정
		if(postDTO.getImgFile() != null && !postDTO.getImgFile().isEmpty()) {
			String savedFileName = saveFile(postDTO.getImgFile());
			postDTO.setPostImg(savedFileName);
		}
		
//		(2) 게시글 수정
		postService.update(postDTO);
		
//		(3) 수정된 게시글 상세 페이지로 이동
		return "redirect:/post/" + postId;
	}
	
//	6) 게시글 삭제
	@PostMapping("/post/delete/{postId}")
	public String delete(@PathVariable("postId") int postId, 
				HttpSession session) {
//		(1) 로그인 확인
		if(session.getAttribute("loginMember") == null) {
//			비로그인 => 로그인 화면으로 이동
			return "redirect:/member/login-form";
		}
		
//		(2) 권한 확인
		PostDTO existPost = postService.findById(postId);
//		삭제하려는 게시글에 대한 정보 가져오기
		MemberDTO loginMember = (MemberDTO)session.getAttribute("loginMember");
//		로그인 회원 정보 가져오기
		
//		작성자 회원Id vs 로그인 회원 Id
		if(loginMember.getMemberId() != existPost.getMemberId()) {
			return "redirect:/post/"+postId;
		}
		
//		(3) 게시글 삭제
		postService.delete(postId);
		
//		(4) 삭제 완료 후 메인 화면으로 이동
		return "redirect:/";
		
		
	}
	
	

}








>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
