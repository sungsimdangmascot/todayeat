<<<<<<< HEAD
// comment.js - 게시글 상세 페이지 댓글


// -- 페이지 로드시 댓글 목록 자동 조회
loadComments();


// 1) 댓글 목록 조회
function loadComments() {
    fetch('/comment/' + postId) // get요청
        .then(function(response) {
            console.log(response);
            return response.json(); // JSON배열 변환
        })
        .then(function(comments) {
            const list = document.getElementById("comment-list");

            // 기존 목록 초기화(새로고침 시 중복 방지)
            list.innerHTML = '';

            // 댓글 수 업데이트
            document.getElementById("comment-count").textContent = comments.length;

            if (comments.length === 0) {
                list.innerHTML = '<p class="text-muted text-sm" style="padding:16px 0;">첫 번째 댓글을 작성해보세요!</p>';
                return;
            }
			
			// 각 댓글을 카드로 만들어서 목록에 추가
			comments.forEach(function(comment){
				// comment : 1개의 댓글 객체
				list.appendChild(renderComment(comment)); // 목록 자식 요소로 추가
			});
        });
}

// 2) 댓글 카드 생성
// 댓글 객체 1개를 받아 <div class='comment-item'> 요소를 만들어 반환
function renderComment(comment){
	const item = document.createElement("div");
	item.className = "comment-item";
	
	const header = document.createElement("div");
	header.className = "comment-header";
	
	// 작성자 정보 영역(이름 + 날짜)
	const authorArea = document.createElement("div");
	authorArea.className="flex gap-8";
	authorArea.style.alignItems = 'center';
	
	const author = document.createElement("strong");
	author.className = "comment-author";
	author.textContent = comment.memberName;
	
	const date = document.createElement("span");
	date.className = "comment-date";
	date.textContent = comment.commentCreatedAt;
	
	authorArea.appendChild(author);
	authorArea.appendChild(date);
	header.appendChild(authorArea);
	
	// 본인 댓글에만 삭제 버튼 추가!
	// (myComment는 서버에서 판단)
	if(comment.myComment){
		const deleteBtn = document.createElement('button');
		deleteBtn.className = "btn btn-danger btn-sm";
		deleteBtn.textContent = "삭제";
		deleteBtn.onclick = function(){
			// 댓글 삭제 요청 실행
			deleteComment(comment.commentId);
		};
		header.appendChild(deleteBtn);
	}
	
	
	item.appendChild(header);
	
	// 댓글 본문
	const body = document.createElement('div');
	body.className = "comment-body";
	body.textContent = comment.commentContent;
	item.appendChild(body);
	
	return item;
}

// 3) 댓글 등록
function writeComment(){
	// (1) 로그인 확인
	if(!isLoggedIn){
		alert("로그인이 필요합니다.");
		window.location.href="/member/login-form";
		return;
	}
	// (2) 입력값 확인
	const input = document.getElementById('comment-input');
	const content = input.value.trim(); // 앞뒤 공백 제거
	if(!content){ // 댓글 내용이 없다면
		alert("댓글 내용을 입력하세요.");
		return;
	}
	
	// (3) 서버에 댓글 등록 요청(JSON body 전송)
	fetch("/comment/write",{
		method:'POST',
		headers:{'Content-Type':'application/json'}, 
		// JSON 형식으로 보낸다고 서버에 알림
		body:JSON.stringify({postId:postId, commentContent:content})
		// 객체 -> json 문자열로 반환
	})
	.then(function(response){
		if(response.status === 401){
			// 로그인 인증 오류
			window.location.href = "/member/login-form";
			return null;
		}
		return response.json();
	})
	.then(function(data){
		if(!data){
			// 데이터가 비어있다면 
			return;
		}
		input.value =''; // 입력창 초기화
		loadComments(); // 목록 새로고침
	});
}

// 4) 댓글 삭제
function deleteComment(commentId){
	if(!confirm("댓글 삭제하시겠습니까?")){
		return; 
	}
	fetch("/comment/delete/" + commentId, {method:"POST"})
		.then(function(response){
			console.log("response : ", response);
			if(response.status === 401){
				window.location.href = "/member/login-form";
				return null;
			}
			return response.json(); // 응답을 json으로 변환
		})
		.then(function(data){
			console.log("data : ", data);
			if(!data){
				return;
			}
			if(data.success){
				loadComments(); // 목록 새로고침
			}
		});
	
}

















=======
// comment.js - 게시글 상세 페이지 댓글


// -- 페이지 로드시 댓글 목록 자동 조회
loadComments();


// 1) 댓글 목록 조회
function loadComments() {
    fetch('/comment/' + postId) // get요청
        .then(function(response) {
            console.log(response);
            return response.json(); // JSON배열 변환
        })
        .then(function(comments) {
            const list = document.getElementById("comment-list");

            // 기존 목록 초기화(새로고침 시 중복 방지)
            list.innerHTML = '';

            // 댓글 수 업데이트
            document.getElementById("comment-count").textContent = comments.length;

            if (comments.length === 0) {
                list.innerHTML = '<p class="text-muted text-sm" style="padding:16px 0;">첫 번째 댓글을 작성해보세요!</p>';
                return;
            }
			
			// 각 댓글을 카드로 만들어서 목록에 추가
			comments.forEach(function(comment){
				// comment : 1개의 댓글 객체
				list.appendChild(renderComment(comment)); // 목록 자식 요소로 추가
			});
        });
}

// 2) 댓글 카드 생성
// 댓글 객체 1개를 받아 <div class='comment-item'> 요소를 만들어 반환
function renderComment(comment){
	const item = document.createElement("div");
	item.className = "comment-item";
	
	const header = document.createElement("div");
	header.className = "comment-header";
	
	// 작성자 정보 영역(이름 + 날짜)
	const authorArea = document.createElement("div");
	authorArea.className="flex gap-8";
	authorArea.style.alignItems = 'center';
	
	const author = document.createElement("strong");
	author.className = "comment-author";
	author.textContent = comment.memberName;
	
	const date = document.createElement("span");
	date.className = "comment-date";
	date.textContent = comment.commentCreatedAt;
	
	authorArea.appendChild(author);
	authorArea.appendChild(date);
	header.appendChild(authorArea);
	
	// 본인 댓글에만 삭제 버튼 추가!
	// (myComment는 서버에서 판단)
	if(comment.myComment){
		const deleteBtn = document.createElement('button');
		deleteBtn.className = "btn btn-danger btn-sm";
		deleteBtn.textContent = "삭제";
		deleteBtn.onclick = function(){
			// 댓글 삭제 요청 실행
			deleteComment(comment.commentId);
		};
		header.appendChild(deleteBtn);
	}
	
	
	item.appendChild(header);
	
	// 댓글 본문
	const body = document.createElement('div');
	body.className = "comment-body";
	body.textContent = comment.commentContent;
	item.appendChild(body);
	
	return item;
}

// 3) 댓글 등록
function writeComment(){
	// (1) 로그인 확인
	if(!isLoggedIn){
		alert("로그인이 필요합니다.");
		window.location.href="/member/login-form";
		return;
	}
	// (2) 입력값 확인
	const input = document.getElementById('comment-input');
	const content = input.value.trim(); // 앞뒤 공백 제거
	if(!content){ // 댓글 내용이 없다면
		alert("댓글 내용을 입력하세요.");
		return;
	}
	
	// (3) 서버에 댓글 등록 요청(JSON body 전송)
	fetch("/comment/write",{
		method:'POST',
		headers:{'Content-Type':'application/json'}, 
		// JSON 형식으로 보낸다고 서버에 알림
		body:JSON.stringify({postId:postId, commentContent:content})
		// 객체 -> json 문자열로 반환
	})
	.then(function(response){
		if(response.status === 401){
			// 로그인 인증 오류
			window.location.href = "/member/login-form";
			return null;
		}
		return response.json();
	})
	.then(function(data){
		if(!data){
			// 데이터가 비어있다면 
			return;
		}
		input.value =''; // 입력창 초기화
		loadComments(); // 목록 새로고침
	});
}

// 4) 댓글 삭제
function deleteComment(commentId){
	if(!confirm("댓글 삭제하시겠습니까?")){
		return; 
	}
	fetch("/comment/delete/" + commentId, {method:"POST"})
		.then(function(response){
			console.log("response : ", response);
			if(response.status === 401){
				window.location.href = "/member/login-form";
				return null;
			}
			return response.json(); // 응답을 json으로 변환
		})
		.then(function(data){
			console.log("data : ", data);
			if(!data){
				return;
			}
			if(data.success){
				loadComments(); // 목록 새로고침
			}
		});
	
}

















>>>>>>> d00555a7e09843903191ef38cdbad58bd04b590c
