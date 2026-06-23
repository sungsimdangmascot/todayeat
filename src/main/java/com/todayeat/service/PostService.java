package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todayeat.dto.PostDTO;
import com.todayeat.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostMapper postMapper;
	
//	1) 게시글 저장
	public void write(PostDTO postDTO) {
		postMapper.write(postDTO);
	}
	
//	2) 전체 게시글 목록 조회
	public List<PostDTO> findAll(){
		return postMapper.findAll();
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
	public void delete(int postId) {
		postMapper.delete(postId);
	}
	
//	6) 특정 회원이 작성한 게시글 목록 조회(마이페이지용)
	public List<PostDTO> findByMemberId(int memberId){
		return postMapper.findByMemberId(memberId);
	}
	

}













