# 35일차

## Spring Data JDBC와 MyBatis를 이용한 데이터베이스 연동

📌 학습일 : 2026.09.04

📌 학습 내용 : Spring Data JDBC, CrudRepository, 쿼리 메서드, HikariCP, MyBatis, Mapper, @Select, @Param, 회원·게시글 조회 및 데이터 처리

---

#### 1. Spring Data JDBC 엔티티 클래스

```java
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 엔티티클래스명 {

    @Id
    private long 기본키;

    private String 필드명1;
    private String 필드명2;
    private Integer 필드명3;
}
```

`@Table`을 사용해 데이터베이스 테이블과 연결하고, `@Id`로 기본키에 해당하는 필드를 지정한다.

Lombok 어노테이션을 함께 사용하면 객체 생성과 데이터 처리를 간단하게 작성할 수 있다.

#### 2. CrudRepository

```java
@Repository
public interface 저장소인터페이스명
        extends CrudRepository<엔티티클래스명, 기본키자료형> {
}
```

`CrudRepository`를 상속하면 기본적인 데이터 저장, 조회, 수정, 삭제 기능을 사용할 수 있다.

```java
저장소객체.save(객체명);
저장소객체.findAll();
저장소객체.findById(기본키값);
저장소객체.deleteById(기본키값);
```

#### 3. 쿼리 메서드를 이용한 조건 조회

```java
List<엔티티클래스명> findBy필드명(
        자료형 매개변수명
);

List<엔티티클래스명> findBy필드명1And필드명2(
        자료형 매개변수명1,
        자료형 매개변수명2
);

List<엔티티클래스명> findBy숫자필드GreaterThan(
        Integer 기준값
);
```

메서드 이름에 조회할 필드와 조건을 작성하여 필요한 데이터를 조회할 수 있다.

`And`는 두 조건을 모두 만족하는 데이터를 찾을 때 사용하고, `GreaterThan`은 기준값보다 큰 데이터를 찾을 때 사용한다.

#### 4. save()를 이용한 데이터 저장과 수정

```java
엔티티클래스명 객체명 =
        저장소객체.save(
                엔티티클래스명.builder()
                        .필드명1(값1)
                        .필드명2(값2)
                        .필드명3(값3)
                        .build()
        );
```

새로운 객체를 `save()`에 전달하여 데이터를 저장할 수 있다.

```java
객체명.set필드명(수정값);

저장소객체.save(객체명);
```

저장된 객체의 값을 변경한 뒤 다시 `save()`를 호출하는 방식으로 데이터 수정도 처리할 수 있다.

#### 5. deleteById()를 이용한 데이터 삭제

```java
저장소객체.deleteById(기본키값);

log.info(
        "데이터 삭제 완료: id={}",
        기본키값
);
```

`deleteById()`에 삭제할 데이터의 기본키를 전달하여 해당 데이터를 삭제할 수 있다.

#### 6. ApplicationRunner에서 Repository 실행

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class 실행클래스명
        implements ApplicationRunner {

    private final 저장소인터페이스명 저장소객체;

    @Override
    public void run(
            ApplicationArguments 매개변수명
    ) throws Exception {

        var 결과목록 =
                저장소객체.findAll();

        log.info("{}", 결과목록);
    }
}
```

`ApplicationRunner`를 이용해 Spring Boot 실행 후 Repository의 데이터 처리 기능을 바로 테스트할 수 있다.

`@RequiredArgsConstructor`를 사용하면 `final` 필드에 필요한 객체를 생성자를 통해 주입받을 수 있다.

#### 7. 데이터베이스 연결 설정

```properties
spring.application.name=프로젝트명

spring.datasource.url=jdbc:데이터베이스종류:연결주소
spring.datasource.username=아이디
spring.datasource.password=비밀번호
```

`application.properties`에서 데이터베이스 연결 주소와 계정 정보를 설정한다.

#### 8. HikariCP 설정

```properties
spring.datasource.hikari.maximum-pool-size=최대연결수
spring.datasource.hikari.minimum-idle=최소유지연결수

logging.level.com.zaxxer.hikari=DEBUG
```

HikariCP의 최대 Connection Pool 크기와 최소 유지 Connection 수를 설정할 수 있다.

로그 레벨을 `DEBUG`로 설정하면 HikariCP의 동작 상태를 확인할 수 있다.

#### 9. MyBatis 모델 클래스

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 모델클래스명 {

    private Long 기본키;
    private String 필드명1;
    private String 필드명2;
    private Integer 필드명3;
}
```

MyBatis에서 조회한 데이터를 저장할 모델 클래스에는 테이블 컬럼과 대응되는 필드를 작성한다.

게시글처럼 구조가 다른 데이터는 별도의 모델 클래스로 나누어 관리할 수 있다.

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 게시글모델클래스명 {

    private Long 기본키;
    private String 제목;
    private String 내용;
    private Date 작성일;
    private Date 수정일;
    private Long 작성자번호;
}
```

#### 10. MyBatis Mapper

```java
@Mapper
public interface 매퍼인터페이스명 {

    List<모델클래스명> selectAll();

    모델클래스명 selectById(
            @Param("id") Long 기본키
    );
}
```

`@Mapper`를 사용해 SQL을 처리하는 Mapper 인터페이스를 작성한다.

`@Param`은 SQL에서 사용할 매개변수의 이름을 지정할 때 사용한다.

#### 11. @Select를 이용한 SQL 작성

```java
@Mapper
public interface 매퍼인터페이스명 {

    @Select("SELECT * FROM 테이블명")
    List<모델클래스명> selectAll();

    @Select(
        "SELECT * FROM 테이블명 "
        + "WHERE 기본키컬럼 = #{id}"
    )
    모델클래스명 selectById(
            @Param("id") Long 기본키
    );
}
```

간단한 SELECT문은 `@Select`를 사용하여 Mapper 메서드에 직접 작성할 수 있다.

`#{id}`에는 `@Param("id")`로 전달한 값이 들어간다.

#### 12. MyBatis 조건 조회와 데이터 추가

```java
@Mapper
public interface 매퍼인터페이스명 {

    List<모델클래스명> selectByName(
            @Param("name") String 검색값
    );

    int insertData(
            모델클래스명 객체명
    );
}
```

Mapper에 필요한 메서드를 추가하여 특정 조건의 데이터를 조회하거나 새로운 데이터를 추가할 수 있다.

#### 13. Mapper 의존성 주입

```java
@Component
@Slf4j
public class 실행클래스명
        implements ApplicationRunner {

    @Autowired
    private 매퍼인터페이스명 매퍼객체;

    @Autowired
    private 게시글매퍼인터페이스명 게시글매퍼객체;

    @Override
    public void run(
            ApplicationArguments 매개변수명
    ) throws Exception {

        var 전체목록 =
                매퍼객체.selectAll();

        log.info(
                "전체 목록 = {}",
                전체목록
        );
    }
}
```

Mapper를 주입받아 실행 클래스에서 데이터 조회와 추가 기능을 호출할 수 있다.

#### 14. 전체 데이터와 특정 데이터 조회

```java
var 전체목록 =
        매퍼객체.selectAll();

log.info(
        "전체 목록 = {}",
        전체목록
);
```

```java
var 조회객체 =
        매퍼객체.selectById(기본키값);

log.info(
        "조회 결과 = {}",
        조회객체
);
```

전체 목록을 조회하는 기능과 기본키를 이용해 하나의 데이터를 조회하는 기능을 각각 사용할 수 있다.

#### 15. 회원과 게시글 데이터 입력

```sql
INSERT INTO 회원테이블명(
    이름컬럼,
    이메일컬럼,
    나이컬럼
)
VALUES(
    '이름값',
    '이메일값',
    나이값
);
```

```sql
INSERT INTO 게시글테이블명(
    제목컬럼,
    내용컬럼,
    작성일컬럼,
    수정일컬럼,
    회원번호컬럼
)
VALUES(
    '제목값',
    '내용값',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    회원번호값
);
```

회원과 게시글 데이터를 각각 추가하고, 게시글에는 회원 번호를 저장하여 작성자와 게시글을 연결할 수 있다.

#### 16. 테이블 관계 설정

```sql
CREATE TABLE 게시글테이블명 (
    기본키컬럼 기본키자료형 PRIMARY KEY,
    제목컬럼 VARCHAR(256),
    내용컬럼 VARCHAR(4096),
    작성일컬럼 TIMESTAMP,
    수정일컬럼 TIMESTAMP,
    회원번호컬럼 기본키자료형,

    FOREIGN KEY(회원번호컬럼)
        REFERENCES 회원테이블명(기본키컬럼)
        ON DELETE CASCADE
);
```

게시글의 회원번호 컬럼을 회원 테이블의 기본키와 외래키로 연결한다.

`ON DELETE CASCADE`를 적용하면 부모 데이터가 삭제될 때 연결된 자식 데이터도 함께 삭제되도록 설정할 수 있다.

#### 핵심 정리

- Spring Data JDBC에서는 `CrudRepository`를 이용해 기본적인 CRUD 기능을 사용할 수 있다.
- `save()`는 데이터 저장과 수정에 사용할 수 있다.
- `findAll()`, `findById()`를 이용해 데이터를 조회할 수 있다.
- `deleteById()`를 이용해 기본키를 기준으로 데이터를 삭제할 수 있다.
- Repository의 메서드 이름에 조건을 작성하여 필요한 데이터를 조회할 수 있다.
- `And`, `GreaterThan` 등을 이용해 여러 형태의 조회 조건을 만들 수 있다.
- `@RequiredArgsConstructor`를 이용해 `final` 객체를 생성자 방식으로 주입받을 수 있다.
- `application.properties`에서 데이터베이스 연결 정보와 HikariCP 설정을 관리할 수 있다.
- MyBatis에서는 `@Mapper`를 이용해 SQL을 처리하는 Mapper를 작성한다.
- `@Select`를 이용하면 SELECT문을 Mapper에 직접 작성할 수 있다.
- `@Param`을 이용해 SQL에서 사용할 매개변수를 전달할 수 있다.
- Mapper에 조회와 추가 메서드를 작성하여 필요한 데이터베이스 기능을 구현할 수 있다.
- 회원과 게시글처럼 서로 관련된 테이블은 외래키를 이용해 관계를 설정할 수 있다.
- Spring Data JDBC와 MyBatis를 각각 사용해 데이터베이스에 접근하는 방법을 실습했다.

---


