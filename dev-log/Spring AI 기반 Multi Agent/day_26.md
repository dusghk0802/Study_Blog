# 26일차

## Oracle 및 Java JDBC 데이터베이스 연동

📌 학습일 : 2026.08.24

📌 학습 내용 : Oracle 테이블 생성, JDBC 연결, PreparedStatement, INSERT, UPDATE

---

#### 1. Oracle 테이블 생성 및 조회

```sql
CREATE TABLE users (
    userid        VARCHAR2(50) PRIMARY KEY,
    username      VARCHAR2(50) NOT NULL,
    userpassword  VARCHAR2(50) NOT NULL,
    userage       NUMBER(3) NOT NULL,
    useremail     VARCHAR2(50) NOT NULL
);
```

```sql
DESC users;

SELECT *
FROM users;
```

특정 사용자의 정보 조회

```sql
SELECT userid, username, userpassword, userage, useremail
FROM users
WHERE userid = '사용자아이디';
```

#### 2. JDBC 데이터베이스 연결

```java
Connection conn = null;

Class.forName("oracle.jdbc.OracleDriver");

conn = DriverManager.getConnection(
    "jdbc:oracle:thin:@localhost:1521/xe",
    "사용자계정",
    "비밀번호"
);
```

* `Class.forName()` : Oracle JDBC Driver 로딩
* `DriverManager.getConnection()` : Oracle 데이터베이스 연결
* `Connection` : 데이터베이스 연결 관리

#### 3. PreparedStatement를 이용한 데이터 등록

```java
String sql =
    "INSERT INTO users(userid, username, userpassword, userage, useremail) " +
    "VALUES (?, ?, ?, ?, ?)";

PreparedStatement pstmt = conn.prepareStatement(sql);

pstmt.setString(1, "아이디");
pstmt.setString(2, "이름");
pstmt.setString(3, "비밀번호");
pstmt.setInt(4, 20);
pstmt.setString(5, "이메일");

int rows = pstmt.executeUpdate();

pstmt.close();
```

* `?` : 실제 데이터가 들어갈 위치
* `setString()` : 문자열 값 지정
* `setInt()` : 정수 값 지정
* `executeUpdate()` : SQL 실행 후 변경된 행의 개수 반환

#### 4. PreparedStatement를 이용한 데이터 수정

```java
String sql =
    "UPDATE boards SET " +
    "btitle = ?, " +
    "bcontent = ? " +
    "WHERE bno = ?";

PreparedStatement pstmt = conn.prepareStatement(sql);

pstmt.setString(1, "수정할 제목");
pstmt.setString(2, "수정할 내용");
pstmt.setInt(3, 3);

int rows = pstmt.executeUpdate();

pstmt.close();
```

* `UPDATE` : 기존 데이터 수정
* `WHERE` : 수정할 데이터의 조건 지정
* `executeUpdate()` : 수정된 행의 개수 반환

#### 5. 데이터베이스 연결 종료

```java
if (conn != null) {
    conn.close();
}
```

* `pstmt.close()` : PreparedStatement 자원 반환
* `conn.close()` : 데이터베이스 연결 종료

---

#### 핵심 정리

* `JDBC`를 이용하여 Java와 Oracle을 연결할 수 있다.
* `Connection`은 데이터베이스 연결을 관리한다.
* `PreparedStatement`를 이용하여 SQL문을 실행할 수 있다.
* SQL문의 `?`에 `setString()`, `setInt()` 등으로 값을 지정한다.
* `executeUpdate()`로 `INSERT`, `UPDATE`, `DELETE`를 실행할 수 있다.
* 데이터베이스 작업이 끝나면 `close()`로 사용한 자원을 반환한다.

---

<p align="center">
  <img src="../../projects/mini-project-01/2026.08.24/day_26_1.png" alt="day_26" width="900">
</p>

오늘 처음으로 팀원들과 mini project를 만들기 위해 아이디어 회의를 하고 함께 역할 분담과 구성을 어떻게 할 것 인지 회의를 하였다.

회의 끝에 난 db와 프론트엔드를 맡기로 하였다.

처음 프로젝트를 하는 거라 완전 처음부터 천천히 기본부터 쌓는게 맞다는 생각에 db와 프론트엔드를 하기로 했는데 막상 처음 시작하는게 막막하기는 했지만 하다보니
엄청 어렵지는 않은 것 같다.

그리고 ERD에 대해 오늘 처음 천천히 해보면서 시도해보았는데 물론 처음이라 거의 대부분은 다른 팀원이 했지만 테이블 3개정도 만들면서 전체적인 구조를 작성하는 것을 배웠다는 것에 만족한다.

시간이 생각보다는 짧아서 빠르게 진행해야해서 좀 아쉽긴 하지만 나중에 다른 벡엔드 등을 추가적으로 틈특히 공부해야겠다.
