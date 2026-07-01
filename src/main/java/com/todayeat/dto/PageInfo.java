package com.todayeat.dto;

import lombok.Getter;

// @Data 대신 @Getter만 쓰는 이유 : 
//  계산 결과는 한번 정하면 바꾸지 않아야 하므로 setter가 필요 없음
@Getter
public class PageInfo {
	
//	--- 외부에서 받아오는 값 ----
	private int currentPage; // 현재 페이지 번호(사용자가 요청한 페이지)
	private int totalCount; // 전체 게시글 수(DB에서 count로 가져옴)
	private int pageSize; // 한 페이지에서 보여줄 게시글 수(6개)
	
//	--- 계산해서 만들어지는 값(생성자에서 계산) ---
	private int totalPages;  // 전체 페이지 수
	private int offset; // SQL에 쓸 offset 값(몇 개를 건너뛸지)
	private int startPage;  // 현재 블록의 첫번째 페이지 번호
	private int endPage; // 현재 블록의 마지막 페이지 번호
	private boolean hasPrevBlock; // 이전 블록이 있으면 true(<버튼 표시 여부)
	private boolean hasNextBlock; // 다음 블록이 있으면 true(>버튼 표시 여부)
	
//	--- 생성자 ---------------------------------------
//	new PageInfo(현재 페이지, 전체개수, 페이지크기) 사용하면
//	생성자를 실행하면서 모든 값을 한번에 계산 함
	public PageInfo(int currentPage, int totalCount, int pageSize) {
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		
//		1) 전체 페이지 수 계산
//		Math.ceil() - 소수점이 있으면 올림
//		게시글 43개, 페이지당 6개 -> 43 / 6 = 7.16... => 올림  => 8페이지@
		this.totalPages = (int)Math.ceil((double) totalCount / pageSize);
		
//		2) OFFSET 계산(SQL에서 몇개를 건너뛸지)
//		1페이지 -> (1-1) x 6 = 0 (아무것도 건너뛰지 않음)
//		2페이지 -> (2-1) x 6 = 6 (앞 6개를 건너뛰고 7번째부터)
//		...
		this.offset = (currentPage - 1) * pageSize;
		
//		3) 현재 블록의 시작 페이지 번호 계산
//		1 ~ 5 페이지 : ((1 ~ 5 - 1) / 5) x 5 + 1 =  0 x 5 + 1 = 1
//		6 ~ 10페이지 : ((6 ~ 10 - 1) / 5) x 5 + 1 = 1 x 5 + 1 = 6
//		...
		this.startPage = ((currentPage - 1) / 5) * 5 + 1;
		
//		4) 현재 블록의 끝 페이지 번호 계산
//		시작번호 + 4가 전체 페이지 수를 넘지 않도록 Math.min()으로 제한
//		총 8페이지, startPage = 1 -> min(1+4, 8) = min(5, 8) = 5
//		총 3페이지, startPage = 2 -> min(2+4, 3) = min(6, 3) = 3
		
		this.endPage = Math.min(startPage + 4, totalPages);
		
//		5) 이전 블록 존재 여부 - < 버튼을 보여줄지 결정
//		시작 페이지가 1보다 크다 = 앞에 더 있다 = 이전 블록이 있다
		this.hasPrevBlock = startPage > 1;
		
//		6) 다음 블록 존재 여부 - > 버튼을 보여줄지 결정
//		끝 페이지가 전체 페이지보다 작다 = 뒤에 더 있다 = 다음 블록이 있다
		this.hasNextBlock = endPage < totalPages;
		
	}
	
	
}








