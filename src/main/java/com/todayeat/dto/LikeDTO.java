package com.todayeat.dto;

import lombok.Data;



@Data
public class LikeDTO {
	
//	현재 사용자의 좋아요 상태
//	true : 좋아요를 누른 상태, false : 좋아요를 취소한 상태
	private boolean liked;
	
//	게시글 좋아요 전체 수 
	private int likeCount;

}







