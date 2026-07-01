<<<<<<< HEAD
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todayeat.dto.PostDTO;
import com.todayeat.mapper.CommentMapper;
import com.todayeat.mapper.LikeMapper;
import com.todayeat.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostMapper postMapper;
	private final CommentMapper commentMapper;
	private final LikeMapper likeMapper;
	
//	1) 게시글 저장
	public void write(PostDTO postDTO) {
		postMapper.write(postDTO);
	}
	
//	2) 전체 게시글 목록 조회
	public List<PostDTO> findAll(String keyword, int offset, int size){
		return postMapper.findAll(keyword, offset, size);
	}
	
//	+) 검색 결과 전체 개수 조회
	public int countAll(String keyword) {
		return postMapper.countAll(keyword);
	}
	
//	3) 게시글 상세 조회
	public PostDTO findById(int postId) {
		return postMapper.findById(postId);
	}
	
//	4) 게시글 수정
	public void update(PostDTO postDTO) {
		postMapper.update(postDTO);
	}
	
//	5) 게시글 삭제
	@Transactional
	public void delete(int postId) {
//		FK걸려 있는 댓글 좋아요 삭제
		likeMapper.deleteByPost(postId);
		commentMapper.deleteByPost(postId);
		postMapper.delete(postId);
	}
	
//	6) 특정 회원이 작성한 게시글 목록 조회(마이페이지용)
	public List<PostDTO> findByMemberId(int memberId){
		return postMapper.findByMemberId(memberId);
	}
	
//	7) map 지도용 - 모든 게시글 좌표 목록 반환(페이징 없음)
	public List<PostDTO> findAllForMap(){
		return postMapper.findAllForMap();
	}
	
//	관리자 통계 - 전체 게시글 수
	public int countAllPosts() {
		return postMapper.countAllPosts();
	}
	
//	관리자용 최근 게시글 목록
	public List<PostDTO> findRecentPostsForAdmin(){
		return postMapper.findRecentPostsForAdmin();
	}
	

}













=======
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todayeat.dto.PostDTO;
import com.todayeat.mapper.CommentMapper;
import com.todayeat.mapper.LikeMapper;
import com.todayeat.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostMapper postMapper;
	private final CommentMapper commentMapper;
	private final LikeMapper likeMapper;
	
//	1) 게시글 저장
	public void write(PostDTO postDTO) {
		postMapper.write(postDTO);
	}
	
//	2) 전체 게시글 목록 조회
	public List<PostDTO> findAll(String keyword, int offset, int size){
		return postMapper.findAll(keyword, offset, size);
	}
	
//	+) 검색 결과 전체 개수 조회
	public int countAll(String keyword) {
		return postMapper.countAll(keyword);
	}
	
//	3) 게시글 상세 조회
	public PostDTO findById(int postId) {
		return postMapper.findById(postId);
	}
	
//	4) 게시글 수정
	public void update(PostDTO postDTO) {
		postMapper.update(postDTO);
	}
	
//	5) 게시글 삭제
	@Transactional
	public void delete(int postId) {
//		FK 걸려 있는 댓글 좋아요 삭제
		likeMapper.deleteByPost(postId);
		commentMapper.deleteByPost(postId);
		postMapper.delete(postId);
	}
	
//	6) 특정 회원이 작성한 게시글 목록 조회(마이페이지용)
	public List<PostDTO> findByMemberId(int memberId){
		return postMapper.findByMemberId(memberId);
	}
	
//	7) map 지도용 - 모든 게시글 좌표 목록 반환(페이징 없음)
	public List<PostDTO> findAllForMap(){
		return postMapper.findAllForMap();
	}

//	관리자 통계 - 전체 게시글 수 
	public int countAllPosts() {
		return postMapper.countAllPosts();
	}
	
}













>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
