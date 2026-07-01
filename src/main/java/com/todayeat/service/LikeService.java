<<<<<<< HEAD
package com.todayeat.service;

import org.springframework.stereotype.Service;

import com.todayeat.dto.LikeDTO;
import com.todayeat.mapper.LikeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeMapper likeMapper;

//	1) 좋아요 토글 처리
//	이미 좋아요가 있으면 취소, 없으면 추가
//	처리 후 최신 좋아요 수와 현재 상태를 likeDTO에 담아 반환
	public LikeDTO toggle(int postId, int memberId) {

//		(1) 현재 좋아요 확인
		boolean isLiked = likeMapper.isLiked(postId, memberId) > 0;

//		(2) 토글처리
		if (isLiked) {
//			이미 좋아요를 누른 상태라면 -> 취소
			likeMapper.delete(postId, memberId);
		} else {
//			좋아요를 누른 상태가 아니라면 -> 추가
			likeMapper.insert(postId, memberId);
		}

//		(3) 최신 좋아요 수 조회
		int likeCount = likeMapper.countByPostId(postId);

//		(4) 결과 DTO 구성 후 반환
		LikeDTO result = new LikeDTO();
		result.setLiked(!isLiked); // 토글 후 상태 : 기존과 반대
		result.setLikeCount(likeCount); // 최신 좋아요 수

		return result;

	}
	
//	2) 특정 게시글의 전체 좋아요 수 조회
//	: 상세 페이지 첫 진입 시 초기값을 화면에 표시하기 위해 사용
	public int countByPostId(int postId) {
		return likeMapper.countByPostId(postId);
	}
	
//	3) 특정 사용자가 이 게시글에 좋아요를 눌렀는지 확인
//	상세 페이지 첫 진입 시 버튼 초기 상태(눌린 색/안눌린 색)를 설정하기 위해 사용
	public boolean isLiked(int postId, int memberId) {
		return likeMapper.isLiked(postId, memberId) > 0;
	}

}










=======
package com.todayeat.service;

import org.springframework.stereotype.Service;

import com.todayeat.dto.LikeDTO;
import com.todayeat.mapper.LikeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeMapper likeMapper;

//	1) 좋아요 토글 처리
//	이미 좋아요가 있으면 취소, 없으면 추가
//	처리 후 최신 좋아요 수와 현재 상태를 likeDTO에 담아 반환
	public LikeDTO toggle(int postId, int memberId) {

//		(1) 현재 좋아요 확인
		boolean isLiked = likeMapper.isLiked(postId, memberId) > 0;

//		(2) 토글처리
		if (isLiked) {
//			이미 좋아요를 누른 상태라면 -> 취소
			likeMapper.delete(postId, memberId);
		} else {
//			좋아요를 누른 상태가 아니라면 -> 추가
			likeMapper.insert(postId, memberId);
		}

//		(3) 최신 좋아요 수 조회
		int likeCount = likeMapper.countByPostId(postId);

//		(4) 결과 DTO 구성 후 반환
		LikeDTO result = new LikeDTO();
		result.setLiked(!isLiked); // 토글 후 상태 : 기존과 반대
		result.setLikeCount(likeCount); // 최신 좋아요 수

		return result;

	}
	
//	2) 특정 게시글의 전체 좋아요 수 조회
//	: 상세 페이지 첫 진입 시 초기값을 화면에 표시하기 위해 사용
	public int countByPostId(int postId) {
		return likeMapper.countByPostId(postId);
	}
	
//	3) 특정 사용자가 이 게시글에 좋아요를 눌렀는지 확인
//	상세 페이지 첫 진입 시 버튼 초기 상태(눌린 색/안눌린 색)를 설정하기 위해 사용
	public boolean isLiked(int postId, int memberId) {
		return likeMapper.isLiked(postId, memberId) > 0;
	}

}










>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
