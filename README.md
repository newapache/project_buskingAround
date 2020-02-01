

# GPS기반의 거리공연 어플리케이션 

[![NPM Version][npm-image]][npm-url]
[![Build Status][travis-image]][travis-url]
[![Downloads Stats][npm-downloads]][npm-url]


<p align="center">
  <img width="350" src="/images/busk.png">
</p>

> 각 종  IT 기술이 발달한 시대 흐름 속에서도 여전히 `거리 공연(이하 버스킹)`은 그저 산발적으로 이루어지고 있습니다. 
 
> 공연 관람자는 관람자의 개별 선호에 맞는 버스킹 정보를 찾기가 어려우며, 오프라인으로만 공연을 접할 수 있습니다. 
 
> 이에 `버스킹을 전문으로 하는 방송 SNS` 개발을 통해 공연의 두 주체 간 소통창을 마련하고자 하였습니다. 


## 데모 영상 

[https://www.youtube.com/watch?v=bgiLJ0IMjqk](https://www.youtube.com/watch?v=bgiLJ0IMjqk) 



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





## 개발 환경 


| Server-Side                | Database  | API | IDE
| ---                 | ---    | ---        | ---
| Ubuntu Server 16.04 LTS(AWS EC2)    | Firbase realtime database | Google Map places API      |  Android studio 3.1.3
| Ant Media Server 1.2.6 Release(방송용 서버 구축) | Mysql (AWS RDS)  |       | 
             



![](/images/system.png)


#


## 핵심 기능 

> 실시간 방송 및 채팅 : [LiveVideoBroadcasterActivity.java](https://github.com/newapache/project_buskingAround/blob/master/app/src/main/java/io/antmedia/android/liveVideoBroadcaster/LiveVideoBroadcasterActivity.java)  

 - 1대 N 방송 기능으로,  공연자는 자신의 개인 페이지에서 ‘방송하기' 버튼을 통해 방송을 시작할 수 있습니다. 
 
 방송의 다중 채팅 기능을 통해 온라인 관객과의 소통도 가능합니다.    

 - aws의 ubuntu 환경에 방송 기능을 제공하는 AntMedia 서버를 구축하여 클라이언트와 통신하였습니다. 
 
 공연자 별 고유의 firebase chat 테이블을 생성하여, n명의 관객이 방송 입장 후 채팅방에서 대화할 수 있습니다.
   


#


> 버스킹 지도  : [MainActivity.java](https://github.com/newapache/project_buskingAround/blob/master/app/src/main/java/finalreport/mobile/dduwcom/myapplication/Main/MainActivity.java)  |  [BuskingMap.java](https://github.com/newapache/project_buskingAround/blob/master/app/src/main/java/finalreport/mobile/dduwcom/myapplication/Map/BuskingMap.java)  


- 현재 자신의 위치 또는 원하는 장소의 공연 정보를 확인 할 수 있으며, 검색 위치에 맵마커를 기록할 수 있습니다. 

-  Google map places api를 이용해 장소에 대한 세부 정보를 제공 받을 수 있습니다. 공연자는 자신의 공연 예정 장소에 마커를 찍어 홍보글을 함께 올릴 수 있습니다.



   
   
   

#



## 기능 흐름도
![](/images/flow.png)



## 설치 방법

OS X & 리눅스:

```sh
npm install my-crazy-module --save
```

윈도우:

```sh
edit autoexec.bat
```

## 사용 예제

스크린 샷과 코드 예제를 통해 사용 방법을 자세히 설명합니다.

_더 많은 예제와 사용법은 [Wiki][wiki]를 참고하세요._

## 개발 환경 설정

모든 개발 의존성 설치 방법과 자동 테스트 슈트 실행 방법을 운영체제 별로 작성합니다.

```sh
make install
npm test
```

## 업데이트 내역

* 0.2.1
    * 수정: 문서 업데이트 (모듈 코드 동일)
* 0.2.0
    * 수정: `setDefaultXYZ()` 메서드 제거
    * 추가: `init()` 메서드 추가
* 0.1.1
    * 버그 수정: `baz()` 메서드 호출 시 부팅되지 않는 현상 (@컨트리뷰터 감사합니다!)
* 0.1.0
    * 첫 출시
    * 수정: `foo()` 메서드 네이밍을 `bar()`로 수정
* 0.0.1
    * 작업 진행 중

## 정보

이름 – [@트위터 주소](https://twitter.com/dbader_org) – 이메일주소@example.com

XYZ 라이센스를 준수하며 ``LICENSE``에서 자세한 정보를 확인할 수 있습니다.

[https://github.com/yourname/github-link](https://github.com/dbader/)

## 기여 방법

1. (<https://github.com/yourname/yourproject/fork>)을 포크합니다.
2. (`git checkout -b feature/fooBar`) 명령어로 새 브랜치를 만드세요.
3. (`git commit -am 'Add some fooBar'`) 명령어로 커밋하세요.
4. (`git push origin feature/fooBar`) 명령어로 브랜치에 푸시하세요. 
5. 풀리퀘스트를 보내주세요.

<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki





-
-

