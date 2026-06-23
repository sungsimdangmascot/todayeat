package com.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.todayeat.dto.CommentDTO;

@Mapper
public interface CommentMapper {
	
//	댓글 등록
	void write(CommentDTO commentDTO);
	
//	특정 게시글의 댓글 목록 조회
	List<CommentDTO> findByPostId(int postId);
	
//	댓글 삭제
	void delete(@Param("commentId") int commentId, @Param("memberId") int memberId);
	

}












