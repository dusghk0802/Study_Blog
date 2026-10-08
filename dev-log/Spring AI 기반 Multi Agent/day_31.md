# 31일차

## Java JDBC 게시판 기능 구현 및 CINEHUB 화면설계서 정리

📌 학습일 : 2026.08.31

📌 학습 내용 : Post 클래스 확장, 게시글 목록·상세 조회,
PreparedStatement, ResultSet, NULL 처리, 게시글 작성 기능 구현, CINEHUB
화면설계서 요구사항·API·DB 매핑 및 기능 명세 수정

---

#### 1. Post 클래스 확장

게시글 목록 조회에 필요한 기본 필드에 상세 조회와 게시글 작성에 필요한
필드를 추가하였다.

private int postId;
private int boardId;
private Integer categoryId;
private int authorId;
private Integer movieId;
private String title;
private String content;
private int viewCount;
private int likeCount;
private int commentCount;
private String status;
private String createdAt;
private String updatedAt;

Integer : NULL 값이 들어올 수 있는 숫자 컬럼에 사용

content : 게시글 내용

authorId : 작성자 번호

status : 게시글 상태

createdAt, updatedAt : 작성일과 수정일

#### 2. 게시글 목록 조회

PostDAO에서 posts 테이블의 게시글 목록을 최신순으로 조회하였다.

String sql =
        "SELECT post_id, title, view_count, like_count, comment_count, created_at " +
        "FROM posts " +
        "ORDER BY created_at DESC";

조회 결과는 ResultSet으로 받아 Post 객체에 저장한 뒤 List<Post>에
추가하였다.

while (rs.next()) {
    Post post = new Post();

    post.setPostId(rs.getInt("post_id"));
    post.setTitle(rs.getString("title"));
    post.setViewCount(rs.getInt("view_count"));
    post.setLikeCount(rs.getInt("like_count"));
    post.setCommentCount(rs.getInt("comment_count"));
    post.setCreatedAt(rs.getString("created_at"));

    postList.add(post);
}

#### 3. 게시글 상세 조회

게시글 번호를 전달받아 해당 게시글 한 건의 상세 정보를 조회하는
selectPost() 메서드를 작성하였다.

public Post selectPost(int postId)

String sql =
        "SELECT post_id, board_id, category_id, author_id, movie_id, " +
        "title, content, view_count, like_count, comment_count, " +
        "status, created_at, updated_at " +
        "FROM posts " +
        "WHERE post_id = ?";

PreparedStatement의 ?에 조회할 게시글 번호를 지정하였다.

pstmt.setInt(1, postId);

조회 결과가 존재하면 하나의 Post 객체에 데이터를 저장하여 반환하였다.

#### 4. NULL 값 처리

category_id, movie_id는 NULL 값이 들어올 수 있으므로 getObject()를
사용하였다.

post.setCategoryId(
        (Integer) rs.getObject("category_id")
);

post.setMovieId(
        (Integer) rs.getObject("movie_id")
);

Java의 기본형 int는 null을 저장할 수 없기 때문에 NULL이 가능한 숫자
컬럼은 Integer 타입으로 선언하였다.

게시글 작성 시에도 NULL이 가능한 값은 setNull()을 이용하여 처리하였다.

if (post.getCategoryId() == null) {
    pstmt.setNull(3, Types.INTEGER);
} else {
    pstmt.setInt(3, post.getCategoryId());
}

#### 5. JDBC 코드 오류 수정

Java는 클래스명의 대소문자를 구분하므로 JDBC 객체명을 정확하게 작성해야
한다.

Connection conn = DBConnection.getConnection();
PreparedStatement pstmt = conn.prepareStatement(sql);

잘못 작성한 형태

connection conn
preparedStatement pstmt

수정한 형태

Connection conn
PreparedStatement pstmt

또한 ResultSet에서 created_at을 가져오려면 SELECT문에도 해당 컬럼이
포함되어 있어야 한다.

SELECT post_id, title, view_count, like_count, comment_count, created_at
FROM posts
ORDER BY created_at DESC;

이를 통해 SQL에서 조회하지 않은 컬럼은 ResultSet에서도 가져올 수
없다는 점을 확인하였다.

#### 6. 게시글 목록 및 상세 조회 테스트

PostDAOTest에서 게시글 목록이 존재하는지 확인하고 게시글 정보를
출력하였다.

List<Post> postList = postDAO.selectPostList();

if (postList.isEmpty()) {
    System.out.println("등록된 게시글이 없습니다.");
} else {
    for (Post post : postList) {
        System.out.println("게시글 번호 : " + post.getPostId());
        System.out.println("제목 : " + post.getTitle());
        System.out.println("조회수 : " + post.getViewCount());
        System.out.println("추천수 : " + post.getLikeCount());
        System.out.println("댓글수 : " + post.getCommentCount());
    }
}

특정 게시글의 상세 정보는 게시글 번호를 전달하여 조회하였다.

Post post = postDAO.selectPost(1);

#### 7. 게시글 작성 기능

PostDAO에 새로운 게시글을 저장하는 insertPost() 메서드를 추가하였다.

public int insertPost(Post post)

String sql =
        "INSERT INTO posts (" +
        "post_id, board_id, category_id, author_id, movie_id, " +
        "title, content, view_count, like_count, comment_count, " +
        "status, created_at, updated_at" +
        ") VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0, 0, ?, SYSDATE, SYSDATE)";

Post 객체에 입력된 값을 PreparedStatement에 순서대로 지정하였다.

pstmt.setInt(1, post.getPostId());
pstmt.setInt(2, post.getBoardId());
pstmt.setInt(4, post.getAuthorId());
pstmt.setString(6, post.getTitle());
pstmt.setString(7, post.getContent());
pstmt.setString(8, post.getStatus());

게시글 작성 결과는 executeUpdate()의 반환값으로 확인하였다.

int result = postDAO.insertPost(newPost);

if (result > 0) {
    System.out.println("게시글 작성 성공");
} else {
    System.out.println("게시글 작성 실패");
}

#### 8. 현재 게시판 기능 구현 상태

게시글 목록 조회    완료
게시글 상세 조회    완료
게시글 작성         구현 진행
게시글 수정         다음 단계
게시글 삭제         다음 단계
Server 연결         이후 진행

#### 9. CINEHUB 화면설계서 요구사항 ID 정리

기존 화면설계서에 임시로 작성했던 요구사항 ID를 실제 요구사항 정의서의
ID로 변경하였다.

주요 요구사항은 다음과 같이 화면과 연결하였다.

회원가입 : REQ-AUTH-01, REQ-AUTH-02

로그인 : REQ-AUTH-03, REQ-AUTH-06

영화 검색·추천 : REQ-REC-01 ~ REQ-REC-04

영화 상세·한줄평 : REQ-REC-07

32강 밸런스 게임 : REQ-REC-06

영화 취향 추천 : REQ-REC-05

게시글 목록·작성 : REQ-COMM-01, REQ-COMM-02

게시글 조회수·좋아요·댓글·신고 : REQ-COMM-03 ~ REQ-COMM-06

관리자 회원·게시글·신고 관리 : REQ-ADM-01 ~ REQ-ADM-03

관리자 로그 : REQ-ADM-04

서비스 소개 화면 [ABOUT-01]은 별도의 기능 요구사항이 없으므로 요구사항
ID를 억지로 지정하지 않고 -로 정리하였다.

#### 10. 회원가입 화면 설계 수정

회원가입 화면 [AUTH_REG-01]은 실제 회원가입 API와 DB 구조를 기준으로
정리하였다.

아이디, 비밀번호, 닉네임, 이메일 입력

입력값 필수 여부와 형식 검증

비밀번호는 PBKDF2 방식으로 단방향 해싱하여 저장

회원가입 요청은 POST /api/auth/register

성공 시 로그인 화면 [AUTH_LOGIN-01]로 이동

기존에 작성했던 다음 API는 실제 API 명세에 없으므로 삭제하였다.

GET /api/auth/check-username
GET /api/auth/check-nickname

또한 화면에 비밀번호 확인 입력란이 없으므로 비밀번호의
실시간 일치 비교와 같은 설명도 제거하였다.

#### 11. 로그인 화면 설계 수정

로그인 화면 [AUTH_LOGIN-01]은 아이디·비밀번호 인증과 세션 처리를
중심으로 정리하였다.

POST /api/auth/login

USERS.USERNAME 기준 회원 조회

입력 비밀번호와 저장된 PASSWORD_HASH 검증

로그인 성공 시 SESSIONID 세션 쿠키 발급

회원 계정 상태 확인

인증 실패 또는 정지 계정은 로그인 차단

회원가입 링크 선택 시 [AUTH_REG-01] 이동

#### 12. 영화 추천 화면 설계 수정

영화 추천 화면 [MOV_REC-01]은 단순 키워드 검색이 아니라 실제
하이브리드 추천 흐름을 반영하였다.

POST /api/recommend/hybrid

하이브리드 추천 흐름

사용자 질의
→ Oracle DB 키워드 후보 조회
→ OpenAI Embeddings 질의 벡터화
→ Qdrant 의미 유사도 검색
→ 키워드 후보 + 벡터 후보 결합
→ OpenAI 최종 영화 1편 선정
→ 추천 사유 생성
→ 결과 렌더링

랜덤 추천은 다음 API를 사용한다.

GET /api/recommend/random

DB에서 무작위 영화 30편을 후보로 구성한 뒤 OpenAI가 최종 추천 영화 1편과
추천 사유를 반환하는 구조로 정리하였다.

#### 13. 영화 상세 화면 설계 수정

영화 상세 화면 [MOV_DTL-01]은 영화 상세정보와 한줄평 기능을
구분하였다.

GET /api/movies/{id}

영화 포스터, 제목, 원제, 평점, 인기도, 개봉일, 줄거리 등 상세정보
표시

한줄평 목록 조회

한줄평 등록

한줄평 좋아요 상태 및 수 갱신

영화 검색으로 선택 시 [MOV_REC-01] 이동

현재 API 명세에는 한줄평 등록·좋아요 전용 API 경로가 별도로 정의되어
있지 않으므로 화면설계서에 임의의 API 주소를 작성하지 않았다.

#### 14. 32강 밸런스 게임 화면 설계 수정

밸런스 게임 [BAL_GAME-01]은 32편의 영화를 이용하여 토너먼트를 진행하고
최종 우승작을 선정하는 기능으로 정리하였다.

32강 → 16강 → 8강 → 4강 → 결승

32강 시작 시 참가 영화 32편으로 대진 구성

현재 라운드와 진행 순서 표시

두 영화 중 하나를 선택하면 승자로 처리

다음 대결 정보로 화면 갱신

결승 완료 후 최종 우승 영화 표시

우승작의 TMDB_ID를 이용하여 유사 영화 조회

GET /api/recommend/balance/similar?tmdbId={우승작 TMDB_ID}

유사 영화는 4편을 제공하도록 정리하였다.

#### 15. 영화 취향 추천 화면 설계 수정

취향 추천 화면 [TASTE_QUIZ-01]은 19개 장르별 영화 2편씩 총 38편을
평가하는 구조로 정리하였다.

현재 평가 순서 및 전체 진행 상태 표시

평가 대상 영화의 포스터·제목·평점 표시

사용자가 영화 선호도 선택

선택 결과 저장 후 다음 영화로 갱신

전체 평가 완료 후 AI 기반 취향 분석 및 영화 추천

POST /api/recommend/taste

평가 완료 후 OpenAI가 사용자 취향을 분석하여 추천 영화 1편과 추천 사유를
생성하고 TMDB 세부정보를 연동한다.

화면설계서의 공간과 실제 화면의 번호를 고려하여 별도의 ④ 영역을 추가하지
않고, ③ 선호도 선택 및 취향 추천에 평가 완료 후 추천 처리까지
통합하였다.

#### 16. 서비스 소개 화면 설계 수정

서비스 소개 화면 [ABOUT-01]은 별도 API나 DB 조회 없이 서비스의 목적과
주요 기능을 안내하는 정보성 화면으로 정리하였다.

CINEHUB의 영화 검색·AI 추천·취향 분석·커뮤니티 기능 소개

게시판 이동 버튼 선택 시 [PST_LIST-01] 이동

별도의 요구사항 ID가 없어 참고 요구사항은 -로 표시

#### 17. 게시글 목록 화면 설계 수정

게시글 목록 화면 [PST_LIST-01]은 게시판 카테고리, 검색, 목록 조회,
글쓰기, 페이징 기능으로 정리하였다.

GET /api/posts

주요 요청 조건

boardId
page
keyword

공지·자유·영화 추천 카테고리별 목록 필터링

검색어를 이용한 게시글 검색

게시글 선택 시 [PST_DTL-01] 이동

글쓰기 버튼 선택 시 [PST_FORM-01] 이동

게시글 목록은 페이지당 10건 단위로 조회

게시판 카테고리는 다른 화면으로 이동하는 네비게이션이 아니라 현재 게시글
목록을 변경하는 카테고리 필터로 정리하였다.

#### 18. 게시글 상세 화면 설계 수정

게시글 상세 화면 [PST_DTL-01]은 상세 조회·조회수·좋아요·댓글·신고
기능으로 정리하였다.

GET /api/posts/{id}
POST /api/posts/{id}/like
POST /api/posts/{id}/comments
POST /api/reports

회원별 최초 조회 시에만 게시글 조회수 1 증가

게시글 좋아요는 회원 1명당 1회 기준으로 토글

댓글 등록 성공 시 댓글 목록 갱신

댓글 등록 시 원글 작성자에게 알림 생성

동일 대상에 대한 중복 신고 차단

현재 COMMENTS 구조에는 부모 댓글을 나타내는 컬럼이 없으므로 기존의
계층형 댓글 표현을 제거하였다.

또한 현재 API 명세에는 댓글 수정·삭제 API가 없으므로 댓글 CRUD라고
표현하지 않고 댓글 조회 및 등록으로 정리하였다.

#### 19. 게시글 작성 화면 설계 수정

게시글 작성 화면은 기존의 [PST_FORM-01 / 02]에서 영화 첨부 모달 관련
내용을 제거하고 [PST_FORM-01] 하나로 정리하였다.

게시판 카테고리 선택

게시글 제목 입력

게시글 내용 입력

게시글 등록

게시글 등록 API

POST /api/posts

요청 데이터

boardId
title
content

데이터 변경 요청이므로 X-CSRF-Token을 검증하고, 등록 성공 후 반환된
postId를 기준으로 게시글 상세 화면 [PST_DTL-01]로 이동하도록
정리하였다.

기존에 작성했던 /post/form은 실제 API와 다르므로 삭제하였다.

#### 20. 알림 목록 화면 설계 수정

알림 화면 [NOTI_LIST-01]은 요구사항 REQ-COMM-05를 기준으로
정리하였다.

화면의 번호가 애매하게 나뉘는 문제를 줄이기 위해 알림 영역을 하나의
기능으로 통합하였다.

로그인 사용자의 신규 알림 현황 표시

댓글 등록으로 생성된 알림 목록 조회

알림 내용 및 발생 시점 표시

알림 선택 시 관련 게시글 상세 화면 [PST_DTL-01] 이동

현재 API 명세에는 알림 목록 조회 API가 별도로 정의되어 있지 않으므로
GET /api/notifications와 같은 임의의 API를 추가하지 않았다.

#### 21. 관리자 메인 화면 설계 수정

관리자 메인 화면 [ADM_MAIN-01]은 관리 현황, 회원 제재, 게시글 숨김,
신고 관리 기능으로 정리하였다.

회원 상태 변경

PATCH /api/admin/users/{id}/status

회원 상태를 SUSPENDED 등으로 변경

정지 처리 시 기존 로그인 세션 즉시 만료

게시글 숨김

PATCH /api/admin/posts/{id}/hide

게시글의 숨김 상태 갱신

신고 처리는 요구사항에는 존재하지만 현재 API 명세에는 관리자용 신고 처리
API가 별도로 정의되어 있지 않으므로 기능 수준으로만 작성하였다.

또한 요구사항과 API에 없는 댓글 숨김 기능은 관리자 화면 명세에서
제외하였다.

#### 22. 관리자 로그 화면 설계 수정

관리 로그 화면 [ADM_LOG-01]은 REQ-ADM-04를 기준으로 로그인 로그와
관리자 작업 로그를 조회하도록 정리하였다.

GET /api/admin/logs

주요 조회 조건

type = login / action
page

관리자 메인으로 돌아가기 선택 시 [ADM_MAIN-01] 이동

로그인 로그와 관리자 작업 로그 조회

관리 로그는 10건 단위로 페이징

관리자 작업 로그 : 관리자 ID, 작업 유형, 대상 ID, 상세 내용, 처리
일시

로그인 로그 : 사용자 ID, IP 주소, 로그인 결과, 로그인 일시

ADMIN_ACTION_LOGS에는 IP 주소 컬럼이 없고 LOGIN_LOGS에만 IP 주소가
있으므로 관리자 작업 로그에서 IP 주소 관련 설명을 제거하였다.

#### 23. 화면설계서 작성 시 확인한 기준

오늘 화면설계서를 수정하면서 다음 기준으로 기능 명세를 작성하였다.

화면에 실제 존재하는 기능을 기준으로 번호를 지정한다.

화면 공간이 부족하면 같은 기능 흐름은 하나의 번호로 통합한다.

사진의 빨간 번호와 기능 명세의 번호가 최대한 1:1로 대응되도록
작성한다.

실제 요구사항 정의서의 ID만 사용한다.

API 명세에 없는 API 주소를 임의로 만들지 않는다.

DB 정의서에 없는 컬럼이나 관계를 임의로 작성하지 않는다.

조회, 매핑, 렌더링, 갱신, 이동, 검증 등 구현 동작이
드러나는 표현을 사용한다.

요구사항이나 API에 없는 실시간, 비동기, 계층형, CRUD 등의
표현은 임의로 사용하지 않는다.

단순 정보성 화면은 억지로 요구사항 ID를 연결하지 않는다.

같은 화면에서 데이터만 바뀌는 경우 불필요하게 화면 ID를 분리하지
않는다.

#### 24. 오늘 진행한 CINEHUB 화면설계서 정리 결과

MAIN_HOME-01      메인 화면
AUTH_REG-01       회원가입 화면
AUTH_LOGIN-01     로그인 화면
MOV_REC-01        영화 추천 화면
MOV_DTL-01        영화 상세 화면
BAL_GAME-01       32강 밸런스 게임
TASTE_QUIZ-01     영화 취향 추천
ABOUT-01          서비스 소개
PST_LIST-01       게시글 목록
PST_DTL-01        게시글 상세
PST_FORM-01       게시글 작성
NOTI_LIST-01      알림 목록
ADM_MAIN-01       관리자 메인
ADM_LOG-01        관리 로그

각 화면의 임시 요구사항 ID를 실제 요구사항 정의서와 연결하고, API·DB
구조와 맞지 않는 기능 설명을 수정하였다.
