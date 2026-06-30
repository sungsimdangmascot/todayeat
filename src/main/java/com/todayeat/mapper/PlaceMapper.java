package com.todayeat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.todayeat.dto.PlaceDTO;

@Mapper
public interface PlaceMapper {
	
//	카카오맵 장소 ID로 기존 장소 조회
//	: 이미 저장된 장소인지 확인할 때 사용 
//	- 같은 카카오 장소 ID가 있으면 중복 저장 방지
//	- 없으면 null 반환
	PlaceDTO findByKakaoId(@Param("kakaoPlaceId") String kakaoPlaceId);
	
//	새 장소 저장(insert)
	void save(PlaceDTO placeDTO);

}













