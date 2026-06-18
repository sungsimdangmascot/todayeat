package com.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todayeat.dto.PostDTO;

@Mapper
public interface PostMapper {
   
//  게시글 작성 (INSERT)
   void write(PostDTO postDTO);
   
//  전체 게시글 목록 조회(select + join)
   List<PostDTO> findAll();
   
//  게시글 상세 조회
   PostDTO findById(int postId);
   
// 게시글 수정
   void update(PostDTO postDTO);
   
   
}
