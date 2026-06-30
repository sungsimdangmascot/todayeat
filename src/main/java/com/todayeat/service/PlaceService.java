package com.todayeat.service;

import org.springframework.stereotype.Service;

import com.todayeat.dto.PlaceDTO;
import com.todayeat.dto.PostDTO;
import com.todayeat.mapper.PlaceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	
	private final PlaceMapper placeMapper;
	
//	장소를 저장하고 PLACE_ID를 반환하는 메소드
//	: "있으면 재사용, 없으면 INSERT" => PLACE 테이블에 같은 장소가 
//		두번 저장되지 않도록
//	ex) 음식 배달 앱에서 "강남구 테헤란로 123"을 여러 사람이 등록해도
//		주소 테이블에는 딱 한번만 저장되고, 주문마다 그 주소 번호만 참고하는 것과 
//	    비슷함
	public int getOrSave(PostDTO postDTO) {
		
//		1) 이미 저장된 장소인지 카카오 주소 ID로 조회
		PlaceDTO existing = placeMapper.findByKakaoId(postDTO.getKakaoPlaceId());
		
		if(existing != null) {
//			이미 있는 장소 -> 새로 저장하지 않고 기존 Place_id 반환
			return existing.getPlaceId();
		}
		
//		2) 처음 나온 장소 -> PlaceDTO 생성 후 INSERT
		PlaceDTO newPlace = new PlaceDTO();
		newPlace.setKakaoPlaceId(postDTO.getKakaoPlaceId());
		newPlace.setPlaceName(postDTO.getPlaceName());
		newPlace.setPlaceAddress(postDTO.getPlaceAddress());
		newPlace.setPlaceLat(postDTO.getPlaceLat());
		newPlace.setPlaceLng(postDTO.getPlaceLng());
		
//		INSERT 실행
		placeMapper.save(newPlace);
		
//		3) 방금 생성된 place_id 반환
		return newPlace.getPlaceId();
		
	}
	
	

}













