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
	
//	관리자 통계 - 전체 댓글 수
	int countAllComments();
	
//	특정 게시글 댓글 전부 삭제(그 글이 사라질때 함께)
	void deleteByPost(int postId);
	
//	특정 회원이 쓴 댓글 전부 삭제(그 회원이 사라질때 함께)
	void deleteByMember(int memberId);

}












