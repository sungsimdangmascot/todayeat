package com.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.todayeat.dto.PostDTO;

@Mapper
public interface PostMapper {
	
//	게시글 작성 (INSERT)
	void write(PostDTO postDTO);
	
//	전체 게시글 목록 조회(select + join)
//	- 검색 + 페이징 게시글 목록 조회
//	- @Param : 파라미터가 여러 개이므로 XML에서 이름으로 구별하기 위해 붙임
//	- keyword : 검색어(없으면 빈 문자열 "")
//	- offset : 건너뛸 게시글 수(PageInfo가 계산해서 넘겨줌)
//	- size : 가져올 게시글 수(페이지당 개수 = 6)
	List<PostDTO> findAll(@Param("keyword") String keyword,
						  @Param("offset") int offset,
						  @Param("size") int size);
	
//	검색 결과 전체 개수 조회 : 총 페이지 수 계산(PageInfo 생성자)에 사용
	int countAll(@Param("keyword") String keyword);
	
//  게시글 상세 조회
	PostDTO findById(int postId);
	
//	게시글 수정 - 제목, 내용, 별점
	void update(PostDTO postDTO);
	
//	게시글 삭제 
	void delete(int postId);
	
//	마이페이지 - 특정 회원이 쓴 게시글 목록 조회
	List<PostDTO> findByMemberId(int memberId);
	
//	map 전체 지도용 - 모든 게시글 좌표, 제목, 별점을 한꺼번에 조회
//	페이징 없이 전체 반환
	List<PostDTO> findAllForMap();
}












