//----------------------------------------
// map 페이지 전용 스크립트
// -------------------------------------

document.addEventListener("DOMContentLoaded", function(){
   // 화면 열리면 바로 실행
   
   // -- 지도 초기화 ------------------------
   // 게시글이 없을 때도 지도 자체는 기본 위치(서울 시청)로 표시
   var container = document.getElementById('kakao-map');
   var options = {
      center : new kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청 좌표
      level : 8 
   };
   
   var map = new kakao.maps.Map(container, options);
   
   // 현재 열려 있는 인포윈도우를 추적하는 변수
   // null = 아무것도 열러 있지 않음
   // 다른 다커를 클릭할때 이 변수를 통해 기존 말풍선을 먼저 닫음
   var currentInfoWindow = null;
   
   // --- 서버에서 게시글 좌표 목록 가져오기 -----
   // fetch('/api/map/posts') => MainController.mapPosts() 를 
   //  통해 JSON 배열 수신
   fetch('/api/map/posts')
      .then(function(response){
         // 서버 응답을 json 객체로 변환
         // console.log(response);
         return response.json();
      })
      .then(function(posts){
      //  게시글을  json 배열로 가져옴
         // console.log(posts);
         
         // 게시글이 하나도 없으면 함수 종료
         if(posts.length === 0){
            return;
         }
         
         // LatLngBounds - 모든 마커의 좌표를 담는 범위 상자
         var bounds = new kakao.maps.LatLngBounds();
         
         // 게시글 수 사이드 패널에 표시
         document.getElementById("place-count").textContent = posts.length;
         
         // 각 게시글마다 마커, 인포윈도우 생성
         posts.forEach(function(post){
            var lat = parseFloat(post.placeLat);
            var lng = parseFloat(post.placeLng);
            var position = new kakao.maps.LatLng(lat, lng);
            /*console.log("---------");
            console.log(position);*/
            
            // -- 마커 생성 ----------
            var marker = new kakao.maps.Marker({
               position : position,
               map : map // 이 주도 위에 마커를 표시
            });
            
            // -- 인포윈도우 생성 ----------
            var infoContent = 
               '<div style="padding:10px 12px; min-width:160px; line-height:1.7;">' + 
               '<strong style="font-size:0.9rem;">' + 
               post.placeName + 
               '</strong><br>' +
               '<span style="color:#f39c12;">★ ' + post.postRating +
               '</span>&nbsp' + 
               '<span style="font-size:0.8rem; color:#888;">' +
               post.postTitle + '</span><br>' + 
               '<a href="/post/'+post.postId+'" ' +
               'style="color:#4a90d9; font-size:0.85rem; text-decoration:none;">' + 
               '상세보기 →' + '</a>' + 
               '</div>';
            
            var infoWindow = new kakao.maps.InfoWindow({
               content : infoContent
               // content : 말풍선 안에 표시할 html 문자열
               // 장소명, 별점, 상세보기 링크를 표시
            });
            
            // console.log(infoWindow);
            
            
            // -- 마커 클릭 이벤트 --------------------
            kakao.maps.event.addListener(marker, "click", function(){
               // 이미 열린 인포윈도우가 있으면 먼저 닫기
               if(currentInfoWindow){
                  currentInfoWindow.close();
               }
               
               // 새 인포윈도우 열기 + 현재 열린 것으로 등록
               infoWindow.open(map, marker);
               currentInfoWindow = infoWindow;
            });
            
            // --- 범위 상자에 이 마커의 좌표 추가
            bounds.extend(position);
            
            // --- 사이드 패널에 항목 추가
            addSideItem(post);
            
         });
         
         // 모든 마커가 한 화면에 보이도록 지도 범위 자동 조정
         // (마커 개수와 위치에 따라 줌 레벨, 중심 좌표가 자동 계산)
         map.setBounds(bounds);
      });
   function addSideItem(post){
      var sideList = document.getElementById('side-list');
      
      var item = document.createElement('a');
      item.className = 'nearby-item';
      item.href = '/post/'+post.postId;
      
      // 사진이 있으면 실제 이미지, 없으면 placeholder
      var thumbHtml = post.postImg
         ?'<img src="/upload/'+post.postImg+
         '" class="nearby-thumb" style="object-fit:cover;" alt="사진">'  
         : '<div class="img-placeholder nearby-thumb">사진</div>';
      item.innerHTML = 
         thumbHtml + 
         '<div>' + 
          '<div style="font-weight:bold; font-size:0.88rem;">' 
          + post.postTitle + '</div>' +
          '<div class="stars-sm">★ ' + post.postRating + '</div>' +
          '<div class="text-muted text-sm">' + post.placeName + '</div>'
          + '</div>';
      sideList.appendChild(item);       
   }
   
   // --- 현재 위치로 이동 버튼 ---------------
   // navigator.geolocation - 브라우저가 제공하는 현재 위치 API
   document.getElementById('btn-my-location').addEventListener('click', function(){
      
      // 브라우저가 geolocation을 지원하지 않는 경우
      if(!navigator.geolocation){
         alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
         return;
      }
      
      // 현재 위치 요청
      navigator.geolocation.getCurrentPosition(
         function(position){
            // 성공 : 현재 위치로 지도 중심 이동 + 줌 레벨 확대
            // console.log(position);
            var myLat = position.coords.latitude;
            var myLng = position.coords.longitude;
            map.setCenter(new kakao.maps.LatLng(myLat, myLng));
            map.setLevel(5); // 동네 단위가 보이는 줌 레벨
            
         }, function(){
            // 실패 : 사용자가 위치 권한 허용을 거부했거나 
            // 위치를 가져올 수 없는 경우
            alert("위치 정보를 가져올 수 없습니다.\n브라우저의 위치 권한을 확인해주세요.");
         }
      )
      
      
      
   });
   
   
});









