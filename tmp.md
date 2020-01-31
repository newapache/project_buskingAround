# GPS기반의 거리공연 어플리케이션 


 각 종  IT 기술이 발달한 시대 흐름 속에서도 여전히 `거리 공연(이하 버스킹)`은 그저 산발적으로 이루어지고 있습니다. 
 
 공연 관람자는 관람자의 개별 선호에 맞는 버스킹 정보를 찾기가 어려우며, 오프라인으로만 공연을 접할 수 있습니다. 
 
 이에 `버스킹을 전문으로 하는 방송 SNS` 개발을 통해 공연의 두 주체 간 소통창을 마련하고자 하였습니다. 




## 구현 화면 
<p align="center">
  <img width="700" height="480" src="/images/view2.png">
</p>

* 로그인 

* 현 위치 및 선호도 별 공연 추천  

* 버스킹 지도 

#



<p align="center">
  <img width="700" height="480" src="/images/view1.png">
</p>


* 마이페이지 

* 공연 홍보글 등록 

* 네트워킹 (팔로우 및 채팅) 


#



<p align="center">
  <img width="700" height="480" src="/images/view3.png">
</p>

* 실시간 라이브 방송 및 채팅 

* 지나간 방송 다시보기 

* 관심회원 검색 





## 기술 스택


| Server-Side                | Database  | API | 
| ---                 | ---    | ---        |
| Ubuntu Server 16.04 LTS(AWS EC2)    | Firbase realtime database | Google Map places API      | 
| Ant Media Server 1.2.6 Release(방송용 서버 구축) | mysql (AWS rds)  |       | 
             



![](/images/system.png)


#


## 핵심 코드

### Client Side : [app.js](https://github.com/sangumee/Timeline-auto-generated-web-service/blob/master/public/javascripts/app.js)  
과목 데이터를 받아서 사용자가 수강과목을 선택하고 과목 중복 처리, 시간표 중복 처리등을 처리
### Server Side : [index.js](https://github.com/sangumee/Timeline-auto-generated-web-service/blob/master/routes/index.js)  
주로 페이지 라우팅 부분을 담당하며 각 페이지별로 DB쿼리를 활용하여 데이터 삽입 읽기 처리
### HTML Templates : [Views Folder](https://github.com/sangumee/Timeline-auto-generated-web-service/tree/master/views)  
HTML 페이지로 데이터 뷰 처리





## 프로젝트 흐름도
<img src="https://i.imgur.com/tLwHtAJ.png" />

## 메인페이지
<img src="https://i.imgur.com/sidR7dG.png" />

학생ID를 입력할 경우 DB에 이미 수강신청되어 있는 데이터가 있는지 확인 후 이미 수강신청된 학생의 데이터가 있을 경우 해당 학생의 수강신청 완료페이지로 라우팅됩니다.

만약 수강신청된 데이터가 존재하지 않으면 해당 학생의 수강신청 페이지로 라우팅됩니다.
모든 학생들의 수강신청 데이터를 확인 할 수 있는 Admin 페이지로 이동 가능한 버튼을 제공합니다.

## 수강신청페이지
<img src="https://i.imgur.com/czouCdx.png" />

데이터베이스에서 신청가능한 모든 과목을 첫번째 테이블에 삽입

<img src="https://i.imgur.com/LZzIED1.png" />

디자인의 영역에서는 데이터가 많아질 경우 Table Header를 고정하고 스크롤 창을 만드는 방법을 선택하였습니다.

각각의 열(Row)을 클릭하면 수강신청 전에 신청이 되었을 경우 차지하게 되는 칸들을 임시적으로 표시합니다.

클릭된 열의 요일과 교시 데이트를 저장하여 열들이 클릭될때마다 시간표 테이블의 해당 칸의 CSS를 수정하여 빨간색의 칸으로 표시합니다.

처음에는 HTML Canvas로 표를 작성후 각각의 요일, 시간 데이터를 활용하여 Canvas에 좌표값을 넣는 방법을 고려하였지만 각각의 데이터의 좌표값을 데이터를 입력하는 상황이 있을 경우 해당 좌표값을 입력해주는것은 무리가 있다고 판단하여 테이블을 활용하였습니다.

시간표테이블의 각각의 칸들은 HTML의 id 값이 할당되어있습니다. (ex: 월요일2교시 : mon2, 금요일1교시 : fri1)

<img src="https://i.imgur.com/mYIuNc5.png" />

각 열의 신청 버튼을 클릭하면 해당 과목의 데이터가 아래의 테이블로 데이터가 전달이 되며 신청된 과목들에 대한 ID값을 읽어 시간표 테이블에 신청된 과목의 색을 반영하며 신청과목 이름이 입력됩니다.

수강학점 또한 신청, 삭제와 동시에 총 수강학점이 표시되는 항목으로 반영됩니다.

과목삭제 버튼을 누를경우 해당 열의 데이터의 ID값을 시간표 테이블로 전달하여 지정된 텍스트와 칸 색을 흰색으로 변경하며 해당 열 전체를 제거합니다.

색상은 랜덤 색상 코드를 작성하는 함수를 통해서 동일 과목의 경우 동일 색으로 표시합니다. 수업이 1주일에 한번 있는 1~2학점의 과목의 경우 최종적으로 칸 두개만 입력되게됩니다.

<img src="https://i.imgur.com/RlSKPWq.png" />

시간표 테이블에서 중복이 되는 과목이 있을 경우에 경고창을 발생시키며 해당 항목을 시간표 테이블에 반영하지 않습니다.

처음에 중복되는 과목을 확인하는 방법에 대한 설계를 할때 시간표 테이블을 HTML5 Canvas로 만들어 각각의 요일, 시간데이터를 좌표값으로 변환후 Canvas에 반영 그 이후에 각각의 해당 좌표값의 [Collision Check MDN](https://developer.mozilla.org/en-US/docs/Games/Tutorials/2D_Breakout_game_pure_JavaScript/Collision_detection) 방법으로 서로 해당칸이 충돌이 발생하면 중복값이 있는것으로 개발을 해보려했고 전에 [Udacity Classic Arcade](https://github.com/sangumee/Udacity-Classic-Arcade-Game) 게임 프로젝트에서 캐릭터가 장애물에 충돌할 경우에 대한 알고리즘을 접해보아서 이 방법이 좋을것이라고 생각하였으나 데이터베이스에서 각 과목의 좌표값을 전부 입력해주기에는 무리가 있고, 과목데이터로 시간을 체크하는 것이 아닌, 좌표값으로 설정되는것이므로 오류가 발생할 여지가 높다고 판단하여 시간표 테이블의 각각의 칸에 ID값을 주는 방법을 선택하였습니다.

신청 버튼을 누를때마다 신청된 과목의 요일, 교시의 데이터를 비교용 배열에 넣고, 다른 과목의 신청버튼을 클릭 할때마다 신청하려하는 요일, 교시데이터의 유무를 비교용 배열에서 확인합니다.

Ex) 신청 버튼을 눌러 비교용 배열에 이미 ['mon1', 'mon2', 'tue2', 'tue3'] 데이터가 존재할 경우 위의 이미지 처럼 신청하려고 하는 과목이 ['tue1', 'tue2', 'tur3', 'tur4'] 데이터를 입력할 경우 'tue2' 항목이 중복되므로 해당 과목의 시간표가 충돌한다고 판정을 한 후 추가한 데이터를 반영하지 않습니다.

## 수강신청 완료페이지

<img src="https://i.imgur.com/76Jvz9r.png" />

수강신청 버튼을 클릭할 경우 수강신청 페이지의 input type의 hidden 값으로 추가된 Value 값들을 서버로 전달한 후 쿼리 실행을 통하여 서버로 데이터를 저장합니다. 이후에 신청한 과목들에 대한 테이블을 생성후에 완료페이지로 라우팅합니다.

이 페이지는 메인페이지에서 이미 수강이 완료된 학생ID를 입력할 경우 이 페이지로 라우팅되게 됩니다. (중복 수강신청 방지)

## 관리자 페이지

<img src="https://i.imgur.com/WTtVTPj.png" />

관리자 페이지에서는 수강신청이 완료된 전체 데이터를 확인할 수 있습니다. 해당 페이지는 [DataTables](https://datatables.net/)의 라이브러리를 사용하여 수강신청 리스트들에 대한 UI 및 활용도를 개선하였습니다. 해당 라이브러리에서 제공하는 기능을 기존의 테이블과 접목시켜 정렬, 검색, 페이지 열갯수등을 관리가능합니다.

기존의 수강신청페이지에서 이 라이브러리를 사용하지 않은 이유는 라이브러리를 사용하지 않고 테이블 데이터 처리 방법론을 연구해보는것이 더 도움이 될것이라고 생각하였기 때문입니다.

## Travis CI

[![Build Status](https://travis-ci.org/sangumee/Timeline-auto-generated-web-service.svg?branch=master)](https://travis-ci.org/sangumee/Timeline-auto-generated-web-service)

[Build Test](https://travis-ci.org/sangumee/Timeline-auto-generated-web-service)

## NPM

[NPM Link](https://www.npmjs.com/package/timeline-auto-generated-web-service)

이 프로젝트에는 NPM 저장소에도 소스코드가 공유되어있습니다. 아래 명령어로도 설치가 가능합니다.

    npm i timeline-auto-generated-web-service
