package com.todayeat.dto;

import lombok.Data;

@Data
public class CommentDTO {
	
	private int commentId;
	private int postId;
	private int memberId;
	private String commentContent;
	private String commentCreatedAt;
	
//	---JOIN에서 가져오는 필드
//	MEMBER 테이블과 join해서 작성자 이름을 함께 가져옴
	private String memberName;
	
//	---서버에서 계산해서 설정하는 필드 
//	현재 로그인한 사용자의 댓글인지 여부
//	true : 본인 댓글 => 삭제 버튼 표시
//	false : 타인 댓글 => 삭제 버튼 숨김
//	JSON 으로 변환 시 "myComment":true 형태로 포함됨
	private boolean myComment;
	
	

}







