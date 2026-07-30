# 9일차

## Oracle 인덱스, 뷰, 권한 관리 및 Java 기초

📌 학습일 : 2026.07.30

📌 학습 내용 : INDEX, VIEW, INLINE VIEW, GRANT, REVOKE, ROLE, SYNONYM, Java 주석, main 메서드, 변수, 연산, 출력

## Oracle

### INDEX

#### 1. 고유 인덱스 생성

```sql
CREATE UNIQUE INDEX 인덱스명
ON 테이블명(컬럼명);
```

컬럼의 중복값을 허용하지 않는 고유 인덱스를 생성한다.

#### 2. 비고유 인덱스 생성

```sql
CREATE INDEX 인덱스명
ON 테이블명(컬럼명);
```

중복값을 허용하는 일반 인덱스를 생성한다.

#### 3. 결합 인덱스 생성

```sql
CREATE INDEX 인덱스명
ON 테이블명(컬럼명1, 컬럼명2);
```

두 개 이상의 컬럼을 결합하여 하나의 인덱스를 생성한다.

#### 4. 정렬 방향을 지정한 결합 인덱스 생성

```sql
CREATE INDEX 인덱스명
ON 테이블명(컬럼명1 DESC, 컬럼명2 ASC);
```

각 컬럼에 내림차순 또는 오름차순을 지정하여 결합 인덱스를 생성한다.

#### 5. 함수 기반 인덱스 생성

```sql
CREATE INDEX 인덱스명
ON 테이블명(함수명(컬럼명));
```

컬럼값 자체가 아니라 함수를 실행한 결과를 기준으로 인덱스를 생성한다.

#### 6. 계산식을 이용한 함수 기반 인덱스 생성

```sql
CREATE INDEX 인덱스명
ON 테이블명((컬럼명 - 값) * 값);
```

컬럼에 계산식을 적용한 결과를 기준으로 인덱스를 생성한다.

#### 7. 함수 기반 인덱스를 이용한 조회

```sql
SELECT 컬럼명1, 컬럼명2
FROM 테이블명
WHERE 함수명(컬럼명) = 값;
```

함수 기반 인덱스를 사용할 때는 인덱스를 생성할 때 사용한 것과 같은 함수를 조건절에 작성한다.

#### 8. 인덱스 삭제

```sql
DROP INDEX 인덱스명;
```

생성된 인덱스를 삭제한다.

#### 9. 테이블에 생성된 인덱스 조회

```sql
SELECT INDEX_NAME, UNIQUENESS
FROM USER_INDEXES
WHERE TABLE_NAME = '테이블명';
```

특정 테이블에 생성된 인덱스 이름과 고유 인덱스 여부를 조회한다.

- `UNIQUE` : 중복값을 허용하지 않는 고유 인덱스
- `NONUNIQUE` : 중복값을 허용하는 비고유 인덱스

#### 10. 인덱스가 설정된 컬럼 조회

```sql
SELECT INDEX_NAME, COLUMN_NAME
FROM USER_IND_COLUMNS
WHERE TABLE_NAME = '테이블명';
```

특정 테이블의 인덱스 이름과 인덱스가 설정된 컬럼을 조회한다.

#### 11. 인덱스 재구성

```sql
ALTER INDEX 인덱스명
REBUILD;
```

인덱스를 다시 구성하여 저장 공간과 검색 성능을 개선한다.

### VIEW

#### 12. 단순 뷰 생성

```sql
CREATE VIEW 뷰명
AS
SELECT 컬럼명1, 컬럼명2
FROM 테이블명
WHERE 조건;
```

하나의 테이블에서 필요한 컬럼과 행을 선택하여 뷰를 생성한다.

#### 13. 복합 뷰 생성

```sql
CREATE VIEW 뷰명
AS
SELECT 테이블별칭1.컬럼명,
       테이블별칭2.컬럼명
FROM 테이블명1 테이블별칭1,
     테이블명2 테이블별칭2
WHERE 테이블별칭1.공통컬럼 = 테이블별칭2.공통컬럼
AND 조건;
```

두 개 이상의 테이블을 조인하여 복합 뷰를 생성한다.

#### 14. 그룹 함수를 이용한 뷰 생성

```sql
CREATE VIEW 뷰명
AS
SELECT 그룹컬럼,
       SUM(값컬럼) 합계,
       AVG(값컬럼) 평균
FROM 테이블명
GROUP BY 그룹컬럼;
```

그룹별 합계와 평균 등의 계산 결과를 뷰로 생성한다.

#### 15. 뷰 조회

```sql
SELECT 컬럼명
FROM 뷰명
WHERE 조건;
```

생성된 뷰를 일반 테이블처럼 조회한다.

#### 16. 생성된 뷰 목록과 정의 조회

```sql
SELECT VIEW_NAME, TEXT
FROM USER_VIEWS;
```

현재 사용자가 생성한 뷰의 이름과 뷰를 생성할 때 사용한 SELECT문을 조회한다.

#### 17. 기존 뷰 수정

```sql
CREATE OR REPLACE VIEW 뷰명
AS
SELECT 컬럼명1, 컬럼명2, 추가할컬럼명
FROM 테이블명
WHERE 조건;
```

기존 뷰가 있으면 새로운 내용으로 수정하고, 뷰가 없으면 새로 생성한다.

#### 18. 뷰 삭제

```sql
DROP VIEW 뷰명;
```

생성된 뷰를 삭제한다.

뷰를 삭제해도 원본 테이블과 데이터는 삭제되지 않는다.

### INLINE VIEW

#### 19. 인라인 뷰 기본 형식

```sql
SELECT 컬럼명
FROM (
    SELECT 컬럼명
    FROM 테이블명
    WHERE 조건
) 인라인뷰별칭;
```

`FROM` 절에 작성한 서브쿼리를 하나의 임시 테이블처럼 사용한다.

#### 20. 그룹별 평균을 구하는 인라인 뷰

```sql
SELECT 테이블별칭.컬럼명,
       인라인뷰별칭.평균값
FROM 테이블명 테이블별칭,
     (
         SELECT 그룹컬럼,
                AVG(값컬럼) 평균값
         FROM 테이블명
         GROUP BY 그룹컬럼
     ) 인라인뷰별칭
WHERE 테이블별칭.그룹컬럼 = 인라인뷰별칭.그룹컬럼;
```

인라인 뷰에서 그룹별 평균을 구한 후 원본 테이블과 연결한다.

#### 21. 그룹 평균보다 큰 데이터 조회

```sql
SELECT 테이블별칭.그룹컬럼,
       테이블별칭.이름컬럼,
       테이블별칭.값컬럼,
       인라인뷰별칭.평균값
FROM 테이블명 테이블별칭,
     (
         SELECT 그룹컬럼,
                AVG(값컬럼) 평균값
         FROM 테이블명
         GROUP BY 그룹컬럼
     ) 인라인뷰별칭
WHERE 테이블별칭.그룹컬럼 = 인라인뷰별칭.그룹컬럼
AND 테이블별칭.값컬럼 > 인라인뷰별칭.평균값;
```

각 그룹의 평균보다 큰 값을 가진 데이터를 조회한다.

#### 22. 그룹별 최대값 조회

```sql
SELECT 인라인뷰별칭.그룹컬럼,
       다른테이블별칭.이름컬럼,
       인라인뷰별칭.최대값
FROM (
    SELECT 그룹컬럼,
           MAX(값컬럼) 최대값
    FROM 테이블명
    GROUP BY 그룹컬럼
) 인라인뷰별칭,
다른테이블명 다른테이블별칭
WHERE 인라인뷰별칭.그룹컬럼 = 다른테이블별칭.공통컬럼;
```

인라인 뷰에서 그룹별 최대값을 구한 후 다른 테이블과 조인하여 상세 정보를 조회한다.

### 사용자 및 시스템 권한

#### 23. 사용자 생성

```sql
CREATE USER 사용자명
IDENTIFIED BY 비밀번호
DEFAULT TABLESPACE 기본테이블스페이스명
TEMPORARY TABLESPACE 임시테이블스페이스명;
```

새로운 Oracle 사용자를 생성하고 기본 및 임시 테이블스페이스를 지정한다.

#### 24. 시스템 권한 부여

```sql
GRANT 시스템권한
TO 사용자명;
```

사용자에게 데이터베이스 기능을 사용할 수 있는 시스템 권한을 부여한다.

#### 25. 여러 시스템 권한 부여

```sql
GRANT 권한명1, 권한명2
TO 사용자명;
```

사용자에게 여러 시스템 권한을 한 번에 부여한다.

#### 26. 현재 사용자가 받은 시스템 권한 조회

```sql
SELECT USERNAME, PRIVILEGE, ADMIN_OPTION
FROM USER_SYS_PRIVS;
```

현재 사용자에게 직접 부여된 시스템 권한을 조회한다.

#### 27. 현재 세션의 시스템 권한 조회

```sql
SELECT PRIVILEGE
FROM SESSION_PRIVS;
```

현재 접속한 세션에서 사용할 수 있는 시스템 권한을 조회한다.

#### 28. 테이블 조회 권한 부여

```sql
GRANT SELECT
ON 스키마명.테이블명
TO 사용자명;
```

다른 사용자가 특정 테이블을 조회할 수 있도록 권한을 부여한다.

#### 29. 여러 객체 권한 부여

```sql
GRANT SELECT, INSERT, UPDATE, DELETE
ON 테이블명
TO 사용자명;
```

특정 테이블에 대한 조회, 입력, 수정, 삭제 권한을 부여한다.

#### 30. 특정 컬럼 수정 권한 부여

```sql
GRANT UPDATE(컬럼명1, 컬럼명2)
ON 테이블명
TO 사용자명;
```

테이블 전체가 아니라 지정한 컬럼만 수정할 수 있는 권한을 부여한다.

#### 31. 다른 사용자에게 부여한 객체 권한 조회

```sql
SELECT GRANTEE, TABLE_NAME, PRIVILEGE
FROM USER_TAB_PRIVS_MADE;
```

현재 사용자가 다른 사용자에게 부여한 객체 권한을 조회한다.

#### 32. 다른 사용자에게서 받은 객체 권한 조회

```sql
SELECT OWNER, TABLE_NAME, GRANTOR, PRIVILEGE
FROM USER_TAB_PRIVS_RECD;
```

현재 사용자가 다른 사용자에게서 받은 객체 권한을 조회한다.

#### 33. 객체 권한 회수

```sql
REVOKE 권한명
ON 테이블명
FROM 사용자명;
```

사용자에게 부여했던 객체 권한을 회수한다.

### ROLE

#### 34. ROLE 생성

```sql
CREATE ROLE 롤명;
```

여러 권한을 하나로 묶어 관리하기 위한 Role을 생성한다.

#### 35. 비밀번호가 있는 ROLE 생성

```sql
CREATE ROLE 롤명
IDENTIFIED BY 비밀번호;
```

활성화할 때 비밀번호가 필요한 Role을 생성한다.

#### 36. ROLE에 시스템 권한 부여

```sql
GRANT 시스템권한
TO 롤명;
```

Role에 시스템 권한을 부여한다.

#### 37. ROLE에 객체 권한 부여

```sql
GRANT SELECT, INSERT, DELETE
ON 테이블명
TO 롤명;
```

Role에 특정 테이블을 사용할 수 있는 객체 권한을 부여한다.

#### 38. 사용자에게 ROLE 부여

```sql
GRANT 롤명
TO 사용자명;
```

생성한 Role을 사용자에게 부여한다.

#### 39. 다른 ROLE에 ROLE 부여

```sql
GRANT 롤명1
TO 롤명2;
```

하나의 Role을 다른 Role에 포함시킨다.

#### 40. ROLE의 시스템 권한 조회

```sql
SELECT ROLE, PRIVILEGE
FROM ROLE_SYS_PRIVS;
```

Role에 부여된 시스템 권한을 조회한다.

#### 41. 사용자에게 부여된 ROLE 조회

```sql
SELECT USERNAME, GRANTED_ROLE, DEFAULT_ROLE
FROM USER_ROLE_PRIVS;
```

현재 사용자에게 부여된 Role을 조회한다.

### SYNONYM

#### 42. 동의어 생성 권한 부여

```sql
GRANT CREATE SYNONYM
TO 사용자명;
```

사용자가 개인 동의어를 생성할 수 있도록 권한을 부여한다.

#### 43. 개인 동의어 생성

```sql
CREATE SYNONYM 동의어명
FOR 스키마명.객체명;
```

다른 스키마의 객체를 간단한 이름으로 사용할 수 있도록 개인 동의어를 생성한다.

#### 44. 개인 동의어를 이용한 조회

```sql
SELECT 컬럼명
FROM 동의어명;
```

긴 스키마명과 객체명 대신 동의어를 사용하여 데이터를 조회한다.

#### 45. 개인 동의어 삭제

```sql
DROP SYNONYM 동의어명;
```

현재 사용자가 생성한 개인 동의어를 삭제한다.

#### 46. 공용 동의어 생성

```sql
CREATE PUBLIC SYNONYM 동의어명
FOR 스키마명.객체명;
```

모든 사용자가 사용할 수 있는 공용 동의어를 생성한다.

공용 동의어를 사용하려면 원본 객체에 접근할 수 있는 권한도 필요하다.

#### 47. 공용 동의어 삭제

```sql
DROP PUBLIC SYNONYM 동의어명;
```

생성된 공용 동의어를 삭제한다.

## Java

### Java 기초

#### 1. 클래스 기본 구조

```java
public class 클래스명 {

}
```

Java 코드는 클래스 내부에 작성한다.

클래스 이름은 일반적으로 영문 대문자로 시작한다.

#### 2. main 메서드

```java
public static void main(String[] args) {

}
```

Java 프로그램이 실행될 때 가장 먼저 시작되는 메서드이다.

#### 3. 문자열 출력

```java
System.out.println("출력할 내용");
```

문자열이나 변수의 값을 콘솔에 출력하고 줄을 바꾼다.

#### 4. 한 줄 주석

```java
// 주석 내용
```

한 줄을 주석 처리하며 프로그램 실행에는 영향을 주지 않는다.

단축키 : `Ctrl + /`

#### 5. 여러 줄 주석

```java
/*
여러 줄의
주석 내용
*/
```

여러 줄을 한 번에 주석 처리한다.

단축키 : `Ctrl + Shift + /`

#### 6. 한 줄 복사

```text
Ctrl + D
```

현재 선택한 줄이나 커서가 위치한 줄을 아래에 복사한다.

#### 7. 변수 선언과 초기화

```java
자료형 변수명 = 값;
```

데이터를 저장할 변수를 만들고 처음 값을 저장한다.

#### 8. 정수형 변수

```java
int 변수명 = 정수값;
```

소수점이 없는 정수 데이터를 저장한다.

#### 9. 산술 연산

```java
int 합계 = 값1 + 값2 + 값3;
```

`+`, `-`, `*`, `/` 등의 연산자를 사용하여 계산한다.

#### 10. 평균 계산

```java
int average = (값1 + 값2 + 값3) / 3;
```

여러 값을 더한 후 개수로 나누어 평균을 계산한다.

정수형 변수끼리 나누면 소수점 이하는 저장되지 않는다.

#### 11. 변수값 출력

```java
System.out.println(변수명);
```

변수에 저장된 값을 콘솔에 출력한다.

---

학과별 최대키를 구하고 최대키를 가진 학생의 학과명, 최대키, 이름, 키를 출력 (서재진 키 186으로 변경)

```sql
UPDATE student
SET height = 186
WHERE name = '서재진';
```
```text
1 행 이(가) 업데이트되었습니다.
```
```java
SELECT d.dname 학과명, m.max_height 최대키, s.name 이름, s.height 키
FROM department d, student s, 
     (SELECT deptno, max(height) max_height
      FROM student
      group by deptno) m
where d.deptno = s.deptno
AND s.deptno = m.deptno
AND s.height = m.max_height;
```
<p align="center">
  <img src="../../training/Oracle/2026-07-29/day_08_4.JPG" alt="day_08" width="700">
</p>

```java
public class ScoreAverage {
    public static void main(String[] args) {
        int java = 88;
        int python = 92;
        int html = 84;

        int average = (java + python + html) / 3;

        System.out.println(average);
    }
}
```
<p align="center">
  <img src="../../training/Oracle/2026-07-29/day_08_4.JPG" alt="day_08" width="700">
</p>
세 과목의 점수를 정수형 변수에 저장한 후 평균을 계산하여 출력한다.
<br/><br/><br/>
