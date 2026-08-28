# 29일차

## CINEHUB 프론트엔드 개선 및 게시글 DB 연동

📌 학습일 : 2026.08.27

📌 학습 내용 : CINEHUB UI 개선, 마이페이지, JDBC, DAO, Java HTTP 서버,
JSON, Fetch API

---

#### 1. CINEHUB UI 개선

-   메인, 커뮤니티, 게시글 상세, 마이페이지 화면 디자인 수정
-   버튼 크기와 Hover 효과 등 공통 디자인 통일
-   커뮤니티 및 마이페이지 메뉴에 `active`를 활용한 선택 밑줄 효과 구성
-   마이페이지에 작성한 글, 작성한 댓글, 찜한 영화 영역 구성

#### 2. Java와 Oracle DB 연결

`ojdbc11`을 이용하여 Java와 Oracle DB를 연결하고, DB 연결 기능을 별도의
클래스로 분리하였다.

``` text
DBConnection.java
DBConnectionTest.java
Post.java
PostDAO.java
PostDAOTest.java
```

프로젝트 재실행 후 발생한 JDBC 드라이버 오류는 모듈 종속 요소에
`ojdbc11`을 다시 등록하여 해결하였다.

#### 3. 게시글 데이터 조회 구현

`Post` 클래스를 생성하여 게시글 번호, 제목, 조회수, 추천수, 댓글수를
객체로 관리하였다.

`PostDAO`에서는 Oracle의 `posts` 테이블을 최신순으로 조회하고, 조회
결과를 `Post` 객체로 생성하여 `List<Post>`에 저장하도록 구현하였다.

``` sql
SELECT post_id,
       title,
       view_count,
       like_count,
       comment_count
FROM posts
ORDER BY created_at DESC;
```

#### 4. Java HTTP 서버 및 게시글 API 구현

Java의 `HttpServer`를 이용하여 별도의 Tomcat 없이 서버를 구성하고,
게시글 목록을 요청할 수 있는 `/api/posts` 경로를 생성하였다.

``` text
http://localhost:8080/api/posts
```

`PostDAO`에서 조회한 게시글 목록을 JSON 형태로 반환하도록 구현하였다.

#### 5. HTML과 Java 게시글 데이터 연결

JavaScript의 `fetch()`를 이용하여 커뮤니티 게시글 페이지에서
`/api/posts`의 데이터를 요청하도록 구성하였다.

``` javascript
fetch("http://localhost:8080/api/posts")
  .then(response => response.json())
  .then(posts => {
    // 게시글 목록 출력
  });
```

전체적인 게시글 데이터 처리 흐름은 다음과 같다.

``` text
Oracle DB
    ↓
DBConnection
    ↓
PostDAO
    ↓
List<Post>
    ↓
Java HttpServer
    ↓
JSON
    ↓
Fetch API
    ↓
커뮤니티 게시글 목록
```

---

이제 어느정도 구현하는 건 끝나가고 제대로 구현되었는지 내일부터 확인하면서 버그나 오류가 있는 부분들을 잡아야 할 것 같다.

그리고 다음주부터는 피피티도 만들고 발표 어떻게 할건지 잘 준비해야겠다.
