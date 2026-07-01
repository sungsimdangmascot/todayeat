<<<<<<< HEAD
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todayeat.mapper.CommentMapper;
import com.todayeat.mapper.LikeMapper;
import com.todayeat.mapper.MemberMapper;
import com.todayeat.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

// 관리자 전용 "삭제" 로직
@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final MemberMapper memberMapper;
	private final PostMapper postMapper;
	private final CommentMapper commentMapper;
	private final LikeMapper likeMapper;
	
//	게시글 완전 삭제(관리자 강제 삭제)
//	게시글에는 댓글, 좋아요가 외래키로 붙어 있으므로 자식부터 삭제
//	@Transactional - 셋 중 하나라도 실패하면 전부 취소
	@Transactional
	public void deletePost(int postId) {
		commentMapper.deleteByPost(postId); // 해당 게시글의 댓글 전부
		likeMapper.deleteByPost(postId); // 이 글의 좋아요 전부
		postMapper.delete(postId); // 게시글 본문 삭제
		
	}
	
	// ------- 
//	회원 완전 삭제(강제 탈퇴/탈퇴신청 승인)
	@Transactional
	public void deleteMember(int memberId) {
		
//		1) 해당 회원이 쓴 게시글 번호 목록
		List<Integer> postIds = postMapper.findIdsByMember(memberId);
		
//		2) 각 게시글의 댓글, 좋아요(남이 단 것 포함)부터 지우고 게시글 삭제
		for(int postId : postIds) {
			commentMapper.deleteByPost(postId);
			likeMapper.deleteByPost(postId);
			postMapper.delete(postId);
		}
		
//		3) 해당 회원이 "다른 글"에 단 댓글 삭제
		commentMapper.deleteByMember(memberId);
		
//		4) 해당 회원이 누른 좋아요 삭제
		likeMapper.deleteByMember(memberId);
		
//		5) 소셜 로그인 연결 삭제
		memberMapper.deleteSocialByMember(memberId);
		
//		6) 마지막으로 회원 본인 삭제
		memberMapper.deleteById(memberId);
		
		
	}
	

}














=======
package com.todayeat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.todayeat.mapper.CommentMapper;
import com.todayeat.mapper.LikeMapper;
import com.todayeat.mapper.MemberMapper;
import com.todayeat.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

// 관리자 전용 "삭제" 로직
@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final MemberMapper memberMapper;
	private final PostMapper postMapper;
	private final CommentMapper commentMapper;
	private final LikeMapper likeMapper;
	
//	게시글 완전 삭제(관리자 강제 삭제)
//	게시글에는 댓글, 좋아요가 외래키로 붙어 있으므로 자식부터 삭제
//	@Transactional - 셋 중 하나라도 실패하면 전부 취소
	@Transactional
	public void deletePost(int postId) {
		commentMapper.deleteByPost(postId); // 해당 게시글의 댓글 전부
		likeMapper.deleteByPost(postId); // 이 글의 좋아요 전부
		postMapper.delete(postId); // 게시글 본문 삭제
		
	}
	
	// ------- 
//	회원 완전 삭제(강제 탈퇴/탈퇴신청 승인)
	@Transactional
	public void deleteMember(int memberId) {
		
//		1) 해당 회원이 쓴 게시글 번호 목록
		List<Integer> postIds = postMapper.findIdsByMember(memberId);
		
//		2) 각 게시글의 댓글, 좋아요(남이 단 것 포함)부터 지우고 게시글 삭제
		for(int postId : postIds) {
			commentMapper.deleteByPost(postId);
			likeMapper.deleteByPost(postId);
			postMapper.delete(postId);
		}
		
//		3) 해당 회원이 "다른 글"에 단 댓글 삭제
		commentMapper.deleteByMember(memberId);
		
//		4) 해당 회원이 누른 좋아요 삭제
		likeMapper.deleteByMember(memberId);
		
//		5) 소셜 로그인 연결 삭제
		memberMapper.deleteSocialByMember(memberId);
		
//		6) 마지막으로 회원 본인 삭제
		memberMapper.deleteById(memberId);
		
		
	}
	

}














>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
