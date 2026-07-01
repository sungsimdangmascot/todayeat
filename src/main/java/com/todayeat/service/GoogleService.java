package com.todayeat.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

// 구글 API와 직접 통신하는 역할 담당
@Service
public class GoogleService {
	
	// application.properties에서 데이터 가져오기
	
	@Value("${google.client-id}")
	private String clientId;
	
	@Value("${google.client-secret}")
	private String  clientSecret;
	
	@Value("${google.redirect-uri}")
	private String redirectUri;
	
	// RestClient - 서버에서 외부 HTTP 요청을 보내는 도구
	private final RestClient restClient = RestClient.create();
	
	// ----------------------------------------------------
	// 구글 로그인 페이지 URL 생성
	public String getAuthorizationUrl() {
		
		// 구글 인증 서버 주소 + 필수 파라미터
		
		// scope=email profile - 이메일 프로필(이름) 정보를 요청
		//  	URL에 공백을 직접 넣을 수 없어 %20(공백의 URL 인코딩) 
		//		response_type=code "인가코드를 달라"
		return "https://accounts.google.com/o/oauth2/v2/auth"
				+ "?client_id=" + clientId  // 앱의 클라이언트 ID
				+ "&redirect_uri=" + redirectUri // 로그인후 돌아올 서버주소
				+ "&response_type=code" // 인가코드 방식 요청
				+ "&scope=email%20profile"; // 요청할 정보 범위
	}
	
//	인가코드 -> 액세스 토큰 교환
	public String getAccessToken(String code) {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUri);
		params.add("code", code); // 인가코드
		
//		RestClient로 구글 토큰 서버에 POST 요청 -> 응답 JSON을 Map으로 변환
		Map response = restClient.post()
				.uri("https://oauth2.googleapis.com/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(params)
				.retrieve()
				.body(Map.class);
		return (String) response.get("access_token");
		// 응답 받은 내용중 액세스 토큰 추출
	}
	
//	액세스 토큰 -> 구글 사용자 정보 조회
//	-> 구글 api서버에 get요청으로 로그인한 사람의 정보를 받아옴
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(String accessToken){
		
		return (Map<String, Object>) restClient.get()
				.uri("https://www.googleapis.com/oauth2/v2/userinfo")
				.header("Authorization", "Bearer "+accessToken)
				.retrieve()
				.body(Map.class);
		
		// 응답 json구조
		/*
		 {
		 "id":"123412341231", <- 구글 회원 고유번호(socialKey로 사용)
		 "email": "user@gmail.com",
		 "name" : "홍길동",
		 ...
		 } 
		 */
		
	}
	
	

}







