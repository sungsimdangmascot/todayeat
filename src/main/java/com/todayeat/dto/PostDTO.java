package com.todayeat.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostDTO {
	
	private int postId; 
	private int memberId;
	private Integer placeId;
	private String postTitle;
	private String postContent;
	private String postImg; // DB에 저장할 파일 경로 (ex: /uploads/test.jpg)
	private int postRating; // 별점
	private String postCreatedAt;
	private String postUpdatedAt;
	
//	-----------------------------
//	파일 업로드 
//	write.html의 <input type='file' name="imgFile">과 이름이  
//	일치해야 spring이 폼에서 올린 파일을 필드에 자동으로 넣어줌
	
//	postImg(String)과 역할이 다름
//	- imgFile - 사용자가 폼에서 선택한 실제 파일 객체
//	- postImg - 파일을 서버에 저장한 뒤 DB에 기록할 경로 문자열
	
	private MultipartFile imgFile;
	
//	------- JOIN으로 가져오는 필드 ----------
	private String memberName; // member테이블의 작성자 이름
//	Kakao Map API 연동 후 js가 이 값을 채워줌
	private String kakaoPlaceId;  // 카카오맵 장소 고유 ID
	private String placeName; // 장소명
	private String placeAddress; // 주소
	private String placeLat; // 위도
	private String placeLng; // 경도
	
//	findAll() 쿼리에서 서브쿼리로 계산해서 가져옴
	private int likeCount; // 이 게시글의 좋아요 개수
	private int commentCount; // 이 게시글의 댓글 개수
	
	
	

}






