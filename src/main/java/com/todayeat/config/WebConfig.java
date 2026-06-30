package com.todayeat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.todayeat.interceptor.AdminInterceptor;

// @Configuration - 이 클래스가 spring 설정 클래스임을 알림
// => 서버 시작 시 자동으로 읽혀서 설정이 적용됨
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
//	@Value - application.properties의 값을 자바 변수로 가져옴
//	${todayeat.upload.path} => "c:/upload/todayeat/"값이 
//	밑의 변수에 할당됨
	@Value("${todayeat.upload.path}")
	private String uploadPath;
	
	@Autowired
	private AdminInterceptor adminInterceptor;

//	addResourceHandlers - "어떤 URL 요청이 오면 어떤 폴더에서 파일을 찾을지"
//	등록하는 메서드 spring이 서버 시작 시 이 메서드를 자동으로 호출함
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
//			- 브라우저가 /upload/ 로 시작하는 URL을 요청하면 이 규칙을 적용
//			- ** 는  "그 뒤에 어떤 경로가 오든 상관없음을"의미
			.addResourceHandler("/upload/**")
//			- 위 URL 요청이 왔을 때 실제 파일을 찾는 폴더 경로 
//			- "file:///"은 로컬 파일 시스템을 가리키는 접두사
//			- file:///C:/upload/todayeat 
			.addResourceLocations("file:///" + uploadPath);
	}

//	인터셉터 등록 
//	addInterceptors - 어떤 인터셉터를 어떤 URL에 적용할지 등록하는 메서드 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(adminInterceptor)
			// "/admin" 자체 + "/admin/**" (하위 경로) 모두 등록
			// "/admin/**"만 쓰면 "/admin" 경로 자체는 패턴에서 빠짐
			.addPathPatterns("/admin", "/admin/**");
	}
	
	
	
	

}










