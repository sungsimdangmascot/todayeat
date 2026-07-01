// page-data.js - 서버에서 보내준 데이터를 전역변수로 변환

// HTML의 <div id='page-data'> 태그에 저장된 값을 잃어
// 전역(window)변수로 설정
// -> like.js, comment.js 에서 postId, isLoggedIn을 
//    사용할 수 있게 설정

(function(){
	// 1) page-data div의  dataset(data-* 속성 모음) 읽기
	let dataset = document.getElementById("page-data").dataset;
	console.log(dataset);
	
	// 2) 현재 게시글 번호
	// dataset.postId는 문자열 "3" 같은 형태로 읽어옴
	// Number()로 숫자로 변환
	window.postId = Number(dataset.postId);
	
	// 3) 로그인 여부
	// data.loggedIn은 문자열 "true" 또는 "false"로 읽어옴
	// === 'true' 비교로 boolean으로 변환
	window.isLoggedIn = dataset.loggedIn === 'true';
	
	
}());

















