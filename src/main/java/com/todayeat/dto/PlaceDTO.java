package com.todayeat.dto;

import lombok.Data;

// 카카오 맵에서 받은 장소 정보를 DB에 저장할 때 사용

@Data
public class PlaceDTO {

	private int placeId; 
	private String kakaoPlaceId; // 카카오맵 장소 고유 ID
	private String placeName; // 장소명
	private String placeAddress; // 주소
	private String placeLat; // 위도
	private String placeLng; // 경도
	
}











