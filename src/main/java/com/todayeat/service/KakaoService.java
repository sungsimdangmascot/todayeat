<<<<<<< HEAD
package com.todayeat.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.mysql.cj.x.protobuf.MysqlxResultset.ContentType_BYTES;

@Service
public class KakaoService {
	
	// application.properties의 kakao.rest-api-key값 자동 주입
	@Value("${kakao.rest-api-key}")
	private String restApiKey;
	
	// kakao.redirect-uri 값 자동 주입
	@Value("${kakao.redirect-uri}")
	private String redirectUri;
	
	// RestClient - 서버에서 외부 HTTP 요청을 보내는 도구
	// create()로 기본 설정으로 생성
	private final RestClient restClient = RestClient.create();
	
	
//	===============================================
//	인가코드 -> 액세스 토큰 교환
//	카카오 서버에 POST 요청으로 인가코드를 보내고, 액세스 토큰을 받아옴
	public String getAccessToken(String code) {
		
//		카카오 토큰 요청에 필요한 파라미터를 Map에 담기
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code"); // 인가코드 방식 고정값
		params.add("client_id", restApiKey); // REST API 키
		params.add("redirect_uri", redirectUri); // 등록한 Redirect URI
		params.add("code", code); // 받은 인가코드
		
//		RestClient로 카카오 토큰 서버에 POST요청
//		응답 JSON -> Map으로 변환
//		응답 예시 : {"access_token":"xxx", "token_type" : "bearer",...}
		Map response = restClient.post()
				.uri("https://kauth.kakao.com/oauth/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED) // form형식으로 전송
				.body(params)
				.retrieve()
				.body(Map.class); // JSON응답을 Map으로 반환
//		응답에서 액세스 토큰만 꺼내 반환
		return (String)response.get("access_token");	
	}
	
//	-----------------------------------------------------
//  액세스 토큰 요청 -> 카카오 사용자 정보 조회
//	카카오 API 서버에 GET 요청으로 로그인한 사람의 정보를 받아옴
	@SuppressWarnings("unchecked") // Map 캐스팅 경고 무시
	public Map<String, Object> getUserInfo(String accessToken){
		
//		카카오에 액세스 토큰 전달 
//		카카오 서버가 이 토큰으로 "이 사람이 누구인지" 확인해줌
		return (Map<String, Object>) restClient.get()
				.uri("https://kapi.kakao.com/v2/user/me")
				.header("Authorization", "Bearer "+ accessToken) // 토큰 인증
				.retrieve()
				.body(Map.class);
		// 응답 JSON 구조 예시
		/*
		 *{
		 *	"id": 123455   <-카카오 회원번호(socialKey로 사용)
		 *   "kakao_account" : {
		 *   	"email" : "user@kakao.com",
		 *   	"profile":{
		 *   		"nickname" : "홍길동"
		 *   		...
		 *   	}
		 *   }
		 *}
		 **/
		
	}
	

}









=======
package com.todayeat.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.mysql.cj.x.protobuf.MysqlxResultset.ContentType_BYTES;

@Service
public class KakaoService {
	
	// application.properties의 kakao.rest-api-key값 자동 주입
	@Value("${kakao.rest-api-key}")
	private String restApiKey;
	
	// kakao.redirect-uri 값 자동 주입
	@Value("${kakao.redirect-uri}")
	private String redirectUri;
	
	// RestClient - 서버에서 외부 HTTP 요청을 보내는 도구
	// create()로 기본 설정으로 생성
	private final RestClient restClient = RestClient.create();
	
	
//	===============================================
//	인가코드 -> 액세스 토큰 교환
//	카카오 서버에 POST 요청으로 인가코드를 보내고, 액세스 토큰을 받아옴
	public String getAccessToken(String code) {
		
//		카카오 토큰 요청에 필요한 파라미터를 Map에 담기
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code"); // 인가코드 방식 고정값
		params.add("client_id", restApiKey); // REST API 키
		params.add("redirect_uri", redirectUri); // 등록한 Redirect URI
		params.add("code", code); // 받은 인가코드
		
//		RestClient로 카카오 토큰 서버에 POST요청
//		응답 JSON -> Map으로 변환
//		응답 예시 : {"access_token":"xxx", "token_type" : "bearer",...}
		Map response = restClient.post()
				.uri("https://kauth.kakao.com/oauth/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED) // form형식으로 전송
				.body(params)
				.retrieve()
				.body(Map.class); // JSON응답을 Map으로 반환
//		응답에서 액세스 토큰만 꺼내 반환
		return (String)response.get("access_token");	
	}
	
//	-----------------------------------------------------
//  액세스 토큰 요청 -> 카카오 사용자 정보 조회
//	카카오 API 서버에 GET 요청으로 로그인한 사람의 정보를 받아옴
	@SuppressWarnings("unchecked") // Map 캐스팅 경고 무시
	public Map<String, Object> getUserInfo(String accessToken){
		
//		카카오에 액세스 토큰 전달 
//		카카오 서버가 이 토큰으로 "이 사람이 누구인지" 확인해줌
		return (Map<String, Object>) restClient.get()
				.uri("https://kapi.kakao.com/v2/user/me")
				.header("Authorization", "Bearer "+ accessToken) // 토큰 인증
				.retrieve()
				.body(Map.class);
		// 응답 JSON 구조 예시
		/*
		 *{
		 *	"id": 123455   <-카카오 회원번호(socialKey로 사용)
		 *   "kakao_account" : {
		 *   	"email" : "user@kakao.com",
		 *   	"profile":{
		 *   		"nickname" : "홍길동"
		 *   		...
		 *   	}
		 *   }
		 *}
		 **/
		
	}
	

}









>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
