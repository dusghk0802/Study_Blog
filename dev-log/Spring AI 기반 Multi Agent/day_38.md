# 38일차

## Spring Boot JPA 연관관계, 테스트 및 Thymeleaf

📌 학습일 : 2026.09.08

📌 학습 내용 : JPA 연관관계, `@ManyToOne`, `@OneToMany`, Cascade, JPA
Auditing, Service 계층, JUnit 테스트, Thymeleaf, Model 데이터 전달

---

## JPA 연관관계

#### 다대일 관계 설정

``` java
@ManyToOne
@JoinColumn(name = "외래키_컬럼명")
private 부모엔티티클래스명 부모객체;
```

여러 개의 자식 데이터가 하나의 부모 데이터와 연결되는 관계를
`@ManyToOne`으로 설정한다. `@JoinColumn`에는 두 테이블을 연결할 외래키
컬럼을 지정한다.

#### 일대다 관계 설정

``` java
@OneToMany(
        mappedBy = "부모객체",
        cascade = CascadeType.ALL,
        orphanRemoval = true
)
private List<자식엔티티클래스명> 자식목록;
```

하나의 부모 데이터가 여러 개의 자식 데이터를 가질 때 `@OneToMany`을
사용한다. `mappedBy`에는 연관관계의 주인이 되는 자식 Entity의 필드명을
지정한다.

`cascade = CascadeType.ALL`과 `orphanRemoval = true`를 설정한 뒤
Postman에서 부모 데이터를 삭제하여 연결된 자식 데이터도 함께 삭제되는
것을 확인하였다.

``` text
POST   /부모경로
GET    /부모경로/{id}
POST   /부모경로/{id}/자식경로
GET    /자식경로/{id}
DELETE /부모경로/{id}
```

## JPA Auditing

#### 생성일과 수정일 자동 관리

``` java
@EntityListeners(AuditingEntityListener.class)
public class 엔티티클래스명 {

    @CreatedDate
    private Date 생성일;

    @LastModifiedDate
    private Date 수정일;
}
```

`@CreatedDate`로 데이터 생성 시간을, `@LastModifiedDate`로 마지막 수정
시간을 관리할 수 있다.

``` java
@EnableJpaAuditing
@SpringBootApplication
public class 실행클래스명 {

    public static void main(String[] args) {
        SpringApplication.run(실행클래스명.class, args);
    }
}
```

JPA Auditing을 사용하기 위해 실행 클래스에 `@EnableJpaAuditing`을
추가한다.

## Service 계층

#### 요청 DTO를 Entity로 변환하여 저장

``` java
public 응답DTO클래스명 생성(요청DTO클래스명 요청객체) {

    var 엔티티객체 = 엔티티클래스명.builder()
            .필드명1(요청객체.get필드명1())
            .필드명2(요청객체.get필드명2())
            .필드명3(요청객체.get필드명3())
            .build();

    저장소객체.save(엔티티객체);

    return 응답변환메서드(엔티티객체);
}
```

Controller에서 받은 요청 DTO를 Entity로 변환한 뒤 Repository를 이용해
데이터베이스에 저장한다.

#### Entity를 Response DTO로 변환

``` java
private 응답DTO클래스명 응답변환메서드(엔티티클래스명 엔티티객체) {

    return 응답DTO클래스명.builder()
            .기본키(엔티티객체.get기본키())
            .필드명1(엔티티객체.get필드명1())
            .필드명2(엔티티객체.get필드명2())
            .필드명3(엔티티객체.get필드명3())
            .build();
}
```

Entity를 그대로 반환하지 않고 필요한 값을 Response DTO로 변환하여 전달할
수 있다.

#### 전체 조회와 단건 조회

``` java
public List<응답DTO클래스명> 전체조회() {
    return 저장소객체.findAll()
            .stream()
            .map(this::응답변환메서드)
            .toList();
}
```

``` java
public 응답DTO클래스명 단건조회(Long 기본키) {

    엔티티클래스명 엔티티객체 =
            저장소객체.findById(기본키)
                    .orElseThrow();

    return 응답변환메서드(엔티티객체);
}
```

`stream()`과 `map()`으로 Entity 목록을 Response DTO 목록으로 변환하고,
`findById()`와 `orElseThrow()`를 이용하여 기본키에 해당하는 데이터를
조회하였다.

#### 여러 데이터 저장

``` java
@Transactional
public List<응답DTO클래스명> 여러개저장(
        List<요청DTO클래스명> 요청목록
) {
    return 요청목록.stream()
            .map(this::생성)
            .toList();
}
```

여러 요청 데이터를 반복해서 저장하고 결과를 목록으로 반환하는 방법을
실습하였다.

## Controller와 Service 연결

``` java
@RestController
@RequestMapping("/공통경로")
public class 컨트롤러클래스명 {

    @Autowired
    private 서비스클래스명 서비스객체;

    @GetMapping("/{id}")
    public 응답DTO클래스명 조회(
            @PathVariable("id") Long 기본키
    ) {
        return 서비스객체.단건조회(기본키);
    }

    @DeleteMapping("/{id}")
    public void 삭제(
            @PathVariable("id") Long 기본키
    ) {
        서비스객체.삭제(기본키);
    }
}
```

Controller에서 Service를 호출하여 조회, 저장, 수정, 삭제 요청을 처리하는
구조를 실습하였다.

## JUnit 테스트

#### 테스트 전 데이터 준비

``` java
@BeforeEach
void 테스트준비() {

    저장소객체.save(
            엔티티클래스명.builder()
                    .필드명1(값1)
                    .필드명2(값2)
                    .필드명3(값3)
                    .build()
    );
}
```

`@BeforeEach`를 이용해 각각의 테스트가 실행되기 전에 필요한 데이터를
준비하였다.

#### 테스트 후 데이터 정리

``` java
@AfterEach
void 테스트정리() {
    저장소객체.deleteAll();
}
```

`@AfterEach`를 이용해 테스트가 끝난 뒤 테스트용 데이터를 삭제하였다.

#### 조회 결과 검증

``` java
@Test
void 전체조회_테스트() {

    List<엔티티클래스명> 결과목록 =
            저장소객체.findAll();

    assertThat(결과목록.size())
            .isEqualTo(예상개수);
}
```

`assertThat()`을 이용해 실제 조회 결과가 예상한 결과와 같은지
확인하였다.

``` java
@Test
void 단건조회_테스트() {

    응답DTO클래스명 결과 =
            서비스객체.단건조회(기본키);

    assertThat(결과.get기본키())
            .isEqualTo(예상기본키);

    assertThat(결과.get필드명1())
            .isEqualTo(예상값);
}
```

Service 메서드를 직접 호출하여 반환된 값도 테스트할 수 있었다.

## Thymeleaf

#### Controller에서 화면 반환

``` java
@Controller
public class 화면컨트롤러클래스명 {

    @GetMapping("/화면경로")
    public String 화면조회() {
        return "템플릿파일명";
    }
}
```

`@Controller`에서 HTML 템플릿 이름을 반환하여 화면을 표시하는 방법을
실습하였다.

#### Thymeleaf 설정

``` html
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
```

HTML에서 `th:` 속성을 사용할 수 있도록 Thymeleaf 네임스페이스를
선언한다.

#### 메시지 출력

``` html
<p th:text="#{메시지키}">기본 문구</p>
```

`#{}`를 이용하여 메시지 값을 화면에 출력할 수 있다.

## Model을 이용한 데이터 전달

#### Controller에서 객체 전달

``` java
@GetMapping("/상세경로")
public String 상세조회(Model model) {

    var 객체명 = 모델클래스명.builder()
            .기본키(값)
            .필드명1(값)
            .필드명2(값)
            .필드명3(값)
            .build();

    model.addAttribute("속성명", 객체명);

    return "템플릿파일명";
}
```

Controller에서 만든 객체를 `Model`에 담아 HTML로 전달하였다.

#### Thymeleaf에서 객체 출력

``` html
<ul>
    <li th:text="${속성명.기본키}">기본값</li>
    <li th:text="${속성명.필드명1}">기본값</li>
    <li th:text="${속성명.필드명2}">기본값</li>
    <li th:text="${속성명.필드명3}">기본값</li>
</ul>
```

`${}`를 이용해 Model에 저장된 객체의 값을 HTML에서 가져와 출력하였다.

## 목록 데이터 반복 출력

``` java
@GetMapping("/목록경로")
public String 목록조회(Model model) {

    var 목록객체 = List.of(
            모델클래스명.builder()
                    .기본키(값1)
                    .필드명1(값1)
                    .build(),

            모델클래스명.builder()
                    .기본키(값2)
                    .필드명1(값2)
                    .build()
    );

    model.addAttribute("목록속성명", 목록객체);

    return "목록템플릿파일명";
}
```

``` html
<table>
    <tr th:each="반복객체 : ${목록속성명}">
        <td th:text="${반복객체.필드명1}">기본값</td>
        <td th:text="${반복객체.필드명2}">기본값</td>
        <td th:text="${반복객체.필드명3}">기본값</td>
    </tr>
</table>
```

`th:each`를 사용하여 List에 들어 있는 데이터를 하나씩 반복하면서
테이블에 출력하였다.

---

## 핵심 정리

-   `@ManyToOne`과 `@OneToMany`으로 Entity 사이의 연관관계를 설정할 수
    있다.
-   `@JoinColumn`으로 외래키 컬럼을 지정할 수 있다.
-   `cascade`와 `orphanRemoval`을 이용하여 연관된 데이터의 삭제를 함께
    처리할 수 있다.
-   `@CreatedDate`, `@LastModifiedDate`로 생성일과 수정일을 관리할 수
    있다.
-   DTO, Entity, Repository, Service, Controller로 역할을 나누어
    데이터를 처리하는 흐름을 실습하였다.
-   `@BeforeEach`와 `@AfterEach`를 이용하여 테스트 전후의 데이터를
    준비하고 정리할 수 있다.
-   `assertThat()`으로 실제 결과와 예상 결과가 같은지 확인할 수 있다.
-   `@Controller`와 `Model`을 이용해 Java의 데이터를 HTML로 전달할 수
    있다.
-   Thymeleaf의 `${}`로 객체의 값을 출력하고 `th:each`로 목록을 반복
    출력할 수 있다.
-   JPA 데이터 처리부터 테스트와 Thymeleaf 화면 출력까지 연결되는 흐름을
    확인하였다.

---
