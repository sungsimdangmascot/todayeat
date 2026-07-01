<<<<<<< HEAD
package com.todayeat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
//	 1) 좋아요 추가
//	@Param :  파라미터가 2개 이상일때 MyBatisrk 이름으로 구분할 수 있도록 명시
	void insert(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	2) 좋아요 취소
	void delete(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	3) 현재 사용자가 이 게시글에 좋아요를 눌렀는지 확인
//	COUNT(*) 결과 반환 - 1: 이미 눌렀음, 0: 아직 안 눌렀음
	int isLiked(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	4) 특정 게시글의 좋아요 수
//	파라미터(매개변수)가 1개이므로 @Param생략 가능
	int countByPostId(int postId);
	
//	5) 관리자 페이지 : 연쇄 삭제
	
//	특정 게시글의 좋아요 전부 삭제(그 글이 사라질때 함께)
	void deleteByPost(int postId);
	
//	특정 회원이 누른 좋아요 전부 삭제(그 회원이 사라질때 함께)
	void deleteByMember(int memberId);
	

}









=======
package com.todayeat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
//	 1) 좋아요 추가
//	@Param :  파라미터가 2개 이상일때 MyBatisrk 이름으로 구분할 수 있도록 명시
	void insert(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	2) 좋아요 취소
	void delete(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	3) 현재 사용자가 이 게시글에 좋아요를 눌렀는지 확인
//	COUNT(*) 결과 반환 - 1: 이미 눌렀음, 0: 아직 안 눌렀음
	int isLiked(@Param("postId") int postId, @Param("memberId") int memberId);
	
//	4) 특정 게시글의 좋아요 수
//	파라미터(매개변수)가 1개이므로 @Param생략 가능
	int countByPostId(int postId);
	
//	5) 관리자 페이지 : 연쇄 삭제
	
//	특정 게시글의 좋아요 전부 삭제(그 글이 사라질때 함께)
	void deleteByPost(int postId);
	
//	특정 회원이 누른 좋아요 전부 삭제(그 회원이 사라질때 함께)
	void deleteByMember(int memberId);
	

}









>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
