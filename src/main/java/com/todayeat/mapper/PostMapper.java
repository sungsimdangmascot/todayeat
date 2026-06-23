package com.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todayeat.dto.PostDTO;

@Mapper
public interface PostMapper {
	
//	게시글 작성 (INSERT)
	void write(PostDTO postDTO);
	
//	전체 게시글 목록 조회(select + join)
	List<PostDTO> findAll();
	
//  게시글 상세 조회
	PostDTO findById(int postId);
	
//	게시글 수정 - 제목, 내용, 별점
	void update(PostDTO postDTO);
	
//	게시글 삭제 
	void delete(int postId);
	
//	마이페이지 - 특정 회원이 쓴 게시글 목록 조회
	List<PostDTO> findByMemberId(int memberId);
}












