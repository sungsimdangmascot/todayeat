// like.js - 게시글 상세페이지 좋아요 기능

function toggleLike(){
	
	// 1) 로그인 확인 - 비로그인이면 로그인 페이지로 이동
	if(!isLoggedIn){ // page-data.js에서 만든 전역변수
		// 로그인이 안된 상태라면 
		alert("로그인이 필요합니다.");
		window.location.href="/member/login-form";
		// get요청
		return; 
	}
	
	// 2) 서버에 좋아요 토글 요청
	fetch('/like/'+ postId, { 
		// postId : page-data.js 에서 만든 전역변수
		method:'POST'
	})
	.then(function(response){
		// console.log(response);
		// 3) 세션 만료(401)이면 로그인 페이지로 이동
		if(response.status === 401){
			window.location.href="/member/login-form";
			return null; 
		}
		// 응답 본문으로 JSON으로 변환
		return response.json();
	})
	.then(function(data){
		// console.log(data);
		if(!data){
			return;
		}
		// 4) 좋아요 수 업데이트
		document.getElementById('like-count').textContent = data.likeCount;  
		
		// 5) 버튼 클래스 변경(색상 토글)
		const btn = document.getElementById("like-btn");
		if(data.liked){
			// 좋아요 현재 누른 상태로 바뀌었다면
			btn.className = 'like-btn liked';  
		} else{
			// 좋아요를 취소했다면
			btn.className = 'like-btn';
		}
		
		
	})
	
	
}




