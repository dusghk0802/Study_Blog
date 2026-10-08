# 32일차

## Java JDBC 게시판 CRUD 기능 구현

📌 학습일 : 2026.09.01

📌 학습 내용 : JDBC 게시글 목록 조회, 상세 조회, 게시글 작성, 게시글
수정, PreparedStatement, NULL 처리, 외래키 제약조건 확인

---

#### 1. 게시글 목록 조회

`selectPostList()` 메서드를 작성하여 `posts` 테이블의 게시글 목록을
조회하였다.

``` java
String sql =
        "SELECT post_id, title, view_count, like_count, comment_count, created_at " +
        "FROM posts " +
        "ORDER BY created_at DESC";
```

`executeQuery()`로 SELECT문을 실행하고 `ResultSet`에서 조회된 값을
`Post` 객체에 저장하였다.

``` java
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
```

조회된 게시글이 여러 개일 수 있으므로 `List<Post>`와 `ArrayList`를
사용하여 저장하였다.

#### 2. 게시글 상세 조회

게시글 번호를 전달받아 하나의 게시글을 조회하는 `selectPost()` 메서드를
작성하였다.

``` java
String sql =
        "SELECT post_id, board_id, category_id, author_id, movie_id, " +
        "title, content, view_count, like_count, comment_count, " +
        "status, created_at, updated_at " +
        "FROM posts " +
        "WHERE post_id = ?";
```

`?`에는 조회할 게시글 번호를 넣었다.

``` java
pstmt.setInt(1, postId);
```

조회 결과가 존재하면 `Post` 객체를 생성하여 각 컬럼의 값을 저장하였다.

#### 3. NULL이 가능한 NUMBER 컬럼 처리

`category_id`와 `movie_id`는 NULL이 들어갈 수 있으므로 `Integer`를
사용하였다.

Oracle의 NUMBER 값을 안전하게 가져오기 위해 `Number`로 받은 후
`intValue()`를 사용하였다.

``` java
Number categoryId = (Number) rs.getObject("category_id");

if (categoryId == null) {
    post.setCategoryId(null);
} else {
    post.setCategoryId(categoryId.intValue());
}
```

`movie_id`도 같은 방식으로 처리하였다.

``` java
Number movieId = (Number) rs.getObject("movie_id");

if (movieId == null) {
    post.setMovieId(null);
} else {
    post.setMovieId(movieId.intValue());
}
```

#### 4. 게시글 작성

게시글을 등록하기 위한 `insertPost()` 메서드를 작성하였다.

``` java
String sql =
        "INSERT INTO posts (" +
        "board_id, category_id, author_id, movie_id, " +
        "title, content, status" +
        ") VALUES (?, ?, ?, ?, ?, ?, ?)";
```

`post_id`는 데이터베이스의 Identity 기능으로 자동 생성되므로
INSERT문에서 직접 입력하지 않았다. 조회수, 추천수, 댓글수, 작성일 등도
데이터베이스의 기본값을 사용하도록 하였다.

``` java
pstmt.setInt(1, post.getBoardId());
pstmt.setInt(3, post.getAuthorId());
pstmt.setString(5, post.getTitle());
pstmt.setString(6, post.getContent());
pstmt.setString(7, post.getStatus());
```

NULL이 가능한 값은 `setNull()`을 이용하였다.

``` java
if (post.getCategoryId() == null) {
    pstmt.setNull(2, Types.NUMERIC);
} else {
    pstmt.setInt(2, post.getCategoryId());
}
```

INSERT처럼 데이터를 변경하는 SQL은 `executeUpdate()`를 사용하였다.

#### 5. 게시글 수정

기존 게시글의 카테고리, 영화, 제목, 내용을 수정하는 `updatePost()`
메서드를 작성하였다.

``` java
String sql =
        "UPDATE posts " +
        "SET category_id = ?, " +
        "movie_id = ?, " +
        "title = ?, " +
        "content = ?, " +
        "updated_at = SYSDATE " +
        "WHERE post_id = ?";
```

게시글 번호를 `WHERE post_id = ?`에 전달하여 특정 게시글만 수정하도록
하였다.

``` java
pstmt.setString(3, post.getTitle());
pstmt.setString(4, post.getContent());
pstmt.setInt(5, post.getPostId());
```

수정 시 `updated_at`은 `SYSDATE`를 사용하여 현재 시간으로 변경하도록
하였다.

#### 6. DAO 테스트 코드 작성

`PostDAOTest`를 이용하여 게시글 목록 조회, 작성, 상세 조회, 수정 기능을
테스트하였다.

처음에는 게시글 목록이 비어 있으면 작성 코드까지 실행되지 않는 문제가
있었다.

``` java
if (postList.isEmpty()) {
    System.out.println("등록된 게시글이 없습니다.");
} else {
    // 작성 코드까지 else 내부에 위치
}
```

게시글이 없는 상태에서도 INSERT 테스트가 실행되어야 하므로 게시글 작성
코드를 `else` 밖으로 분리하였다.

작성 후 목록을 다시 조회하고 실제 게시글 번호를 가져오는 방식도
적용하였다.

``` java
postList = postDAO.selectPostList();

if (!postList.isEmpty()) {
    int postId = postList.get(0).getPostId();
}
```

이를 통해 게시글 번호를 임의로 고정하지 않고 실제 데이터베이스의 게시글
번호를 사용할 수 있도록 하였다.

#### 7. 외래키 제약조건 오류 확인

게시글 작성 테스트에서 다음 Oracle 오류가 발생하였다.

``` text
ORA-02291: 무결성 제약조건(MINIPROJECT.FK_POSTS_AUTHOR)이 위배되었습니다
- 부모 키가 없습니다
```

`posts.author_id`는 `users.user_id`를 참조하는 외래키이다.

테스트에서는 다음과 같이 작성자 번호를 지정하였다.

``` java
newPost.setAuthorId(1);
```

하지만 `users` 테이블에 해당 `user_id`가 존재하지 않아 INSERT가
실패하였다.

외래키가 설정된 컬럼에 값을 추가하려면 부모 테이블에 해당 데이터가 먼저
존재해야 한다.

``` sql
SELECT user_id, login_id
FROM users
ORDER BY user_id;
```

`board_id` 역시 `boards` 테이블을 참조하므로 실제 존재하는 게시판
번호인지 확인해야 한다.

``` sql
SELECT board_id, name
FROM boards
ORDER BY board_id;
```

#### 8. 현재 구현 상태

현재 다음 기능까지 구현하였다.

-   게시글 목록 조회 `selectPostList()`
-   게시글 상세 조회 `selectPost()`
-   게시글 작성 `insertPost()`
-   게시글 수정 `updatePost()`
-   `PostDAOTest`를 이용한 기능 테스트
-   Oracle 외래키 제약조건 오류 원인 확인

게시글 작성 기능의 Java/JDBC 코드는 구현하였지만, 테스트 데이터의
`author_id`에 해당하는 부모 데이터가 없어 실제 INSERT는 아직 성공하지
않은 상태이다.

다음 작업에서는 `users`와 `boards` 테이블의 실제 데이터를 확인한 뒤
올바른 `author_id`, `board_id`로 작성 테스트를 다시 진행하고 게시글 삭제
기능을 구현할 예정이다.

---

#### 핵심 정리

-   SELECT문은 `executeQuery()`를 사용하고 결과를 `ResultSet`으로
    받는다.
-   INSERT와 UPDATE문은 `executeUpdate()`를 사용한다.
-   `PreparedStatement`의 `?`에는 순서에 맞게 값을 설정해야 한다.
-   NULL이 가능한 숫자 컬럼은 `Integer`를 사용하면 NULL을 표현할 수
    있다.
-   Oracle NUMBER 값을 `getObject()`로 가져올 때 `Number`로 받은 뒤
    `intValue()`로 변환할 수 있다.
-   Identity 기본키는 INSERT 시 직접 입력하지 않고 데이터베이스가 자동
    생성하도록 할 수 있다.
-   외래키 컬럼에는 부모 테이블에 실제 존재하는 값을 입력해야 한다.
-   `ORA-02291`은 참조하려는 부모 키가 존재하지 않을 때 발생하는 외래키
    제약조건 오류이다.

---

오늘은 미니 프로젝트 내일 발표해야해서 대본 작성하고 저번에 연습하던 자바를 다시 했는데 확실히 백엔드 부분이 확실히 어려운 것 같다.

앞으로 여유 있는 시간에 계속 이어 나가면서 할 생각이라 여유가 있지만 좀 더 연습이 필요할 것 같고 그래도 처음보다는 점점 나아지고 있는 것 같아서 다행이다.
