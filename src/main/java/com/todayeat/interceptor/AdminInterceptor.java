<<<<<<< HEAD
package com.todayeat.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.todayeat.dto.MemberDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component  - spring이 이 클래스를 자동으로 부품(Bean)을 등록
// WebConfig에서 이 Bean을 가져다 인터셉터로 등록함
@Component
public class AdminInterceptor implements HandlerInterceptor{

	// preHandle - "컨트롤러 실행 전(pre)에 처리(handle)" 메서드
	//  => return true  : 컨트롤러 통과
	//  => return false : 여기서 차단(컨트롤러 실행 안함)
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 
//		로그인한 사람이 관리자인지 확인
//		1) 세션에서 로그인 정보 꺼내기
		MemberDTO loginMember = (MemberDTO) request.getSession()
										.getAttribute("loginMember");
//		2) 로그인 안 했으면 -> 메인으로
		if(loginMember == null) {
			response.sendRedirect("/");
			return false;
		}
		
//		3) 로그인했지만 ADMIN이 아니라면 -> 메인으로
		if(!"ADMIN".equals(loginMember.getMemberRole())) {
			response.sendRedirect("/");
			return false;
		}
		
//		4) ADMIN 확인 완료 -> 통과
		return true;
		
	}

	
	
}
















=======
package com.todayeat.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.todayeat.dto.MemberDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component  - spring이 이 클래스를 자동으로 부품(Bean)을 등록
// WebConfig에서 이 Bean을 가져다 인터셉터로 등록함
@Component
public class AdminInterceptor implements HandlerInterceptor{

	// preHandle - "컨트롤러 실행 전(pre)에 처리(handle)" 메서드
	//  => return true  : 컨트롤러 통과
	//  => return false : 여기서 차단(컨트롤러 실행 안함)
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 
//		로그인한 사람이 관리자인지 확인
//		1) 세션에서 로그인 정보 꺼내기
		MemberDTO loginMember = (MemberDTO) request.getSession()
										.getAttribute("loginMember");
//		2) 로그인 안 했으면 -> 메인으로
		if(loginMember == null) {
			response.sendRedirect("/");
			return false;
		}
		
//		3) 로그인했지만 ADMIN이 아니라면 -> 메인으로
		if(!"ADMIN".equals(loginMember.getMemberRole())) {
			response.sendRedirect("/");
			return false;
		}
		
//		4) ADMIN 확인 완료 -> 통과
		return true;
		
	}

	
	
}
















>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
