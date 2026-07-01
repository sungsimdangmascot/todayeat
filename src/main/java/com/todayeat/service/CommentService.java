<<<<<<< HEAD
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todayeat.dto.CommentDTO;
import com.todayeat.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final CommentMapper commentMapper;
	
//	1) 댓글 등록
//	write() 호출 후 MyBatis가 자동 생성된 commentId를 commentDTO에 채워줌
//	(useGeneratedKeys)
//	채워진 commentDTO를 그대로 반환 => Controller에서 json으로 변환해서 
//			js에 전달
	public CommentDTO write(CommentDTO commentDTO) {
		commentMapper.write(commentDTO);
		return commentDTO;
	}
	
//	2) 특정 게시글의 댓글 목록 조회
	public List<CommentDTO> findByPostId(int postId){
		return commentMapper.findByPostId(postId);
	}
	
//	3) 댓글 삭제
	public void delete(int commentId, int memberId) {
		commentMapper.delete(commentId, memberId);
	}
	
//	관리자 통계 - 전체 댓글 수 
	public int countAllComments() {
		return commentMapper.countAllComments();
	}
	
//	댓글 강제삭제(관리자) 
	public void deleteComment(int commentId) {
		commentMapper.deleteByAdmin(commentId);
	}
	
//	관리자용 최근 댓글 목록(작성자 이름 + 게시글 제목 포함)
	public List<CommentDTO> findRecentCommentsForAdmin(){
		return commentMapper.findRecentCommentsForAdmin();
	}
	

}












=======
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todayeat.dto.CommentDTO;
import com.todayeat.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final CommentMapper commentMapper;
	
//	1) 댓글 등록
//	write() 호출 후 MyBatis가 자동 생성된 commentId를 commentDTO에 채워줌
//	(useGeneratedKeys)
//	채워진 commentDTO를 그대로 반환 => Controller에서 json으로 변환해서 
//			js에 전달
	public CommentDTO write(CommentDTO commentDTO) {
		commentMapper.write(commentDTO);
		return commentDTO;
	}
	
//	2) 특정 게시글의 댓글 목록 조회
	public List<CommentDTO> findByPostId(int postId){
		return commentMapper.findByPostId(postId);
	}
	
//	3) 댓글 삭제
	public void delete(int commentId, int memberId) {
		commentMapper.delete(commentId, memberId);
	}
	
//	관리자 통계 - 전체 댓글 수 
	public int countAllComments() {
		return commentMapper.countAllComments();
	}
	
	

}












>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
