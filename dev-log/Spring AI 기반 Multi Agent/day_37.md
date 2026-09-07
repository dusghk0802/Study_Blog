# 37일차

## Spring Boot JPA 회원·게시글 API 구현

📌 학습일 : 2026.09.07

📌 학습 내용 : DTO, Entity, JpaRepository, Service, Controller, JPA
Auditing, 연관관계, REST API, Postman 테스트

#### 1. 회원 요청·응답 DTO

클라이언트에서 전달받는 데이터와 클라이언트에게 반환하는 데이터를
분리하기 위해 Request DTO와 Response DTO를 사용한다.

``` java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class 요청DTO {
    private String 이름;
    private String 이메일;
    private Integer 나이;
}
```

``` java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class 응답DTO {
    private Long 번호;
    private String 이름;
    private String 이메일;
    private Integer 나이;
}
```

-   Request DTO는 회원 생성 시 전달받을 데이터를 정의한다.
-   Response DTO는 데이터베이스의 Entity를 그대로 반환하지 않고 필요한
    데이터만 전달할 때 사용한다.
-   `@Data`를 사용하여 Getter, Setter 등을 자동 생성한다.
-   `@Builder`를 사용하여 객체를 필요한 값으로 조립할 수 있다.

#### 2. 회원 Entity와 JPA 설정

Entity는 데이터베이스 테이블과 연결되는 클래스이다.

``` java
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class 회원엔티티 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 번호;

    private String 이름;

    @Column(unique = true)
    private String 이메일;

    private Integer 나이;
    private String 비밀번호;
    private Boolean 활성화여부;
}
```

-   `@Entity` : JPA가 관리하는 Entity로 지정한다.
-   `@Id` : 기본키를 지정한다.
-   `@GeneratedValue` : 기본키 값을 자동 생성한다.
-   `@Column(unique = true)` : 해당 컬럼의 중복값을 허용하지 않는다.

#### 3. JpaRepository를 이용한 데이터 처리

`JpaRepository`를 상속하면 기본적인 CRUD 기능을 직접 SQL로 작성하지
않아도 사용할 수 있다.

``` java
@Repository
public interface 회원Repository
        extends JpaRepository<회원엔티티, Long> {
}
```

대표적으로 사용할 수 있는 메서드는 다음과 같다.

``` java
save(객체);          // 등록 및 수정
findAll();           // 전체 조회
findById(번호);      // 한 건 조회
deleteById(번호);    // 삭제
```

#### 4. Service에서 DTO와 Entity 변환

Service에서는 Controller에서 받은 요청 데이터를 Entity로 변환하여
저장하고, 조회한 Entity를 Response DTO로 변환한다.

``` java
public 응답DTO create(요청DTO 요청) {

    var 엔티티 = 회원엔티티.builder()
            .이름(요청.get이름())
            .이메일(요청.get이메일())
            .나이(요청.get나이())
            .활성화여부(true)
            .build();

    repository.save(엔티티);

    return mapToResponse(엔티티);
}
```

Entity를 Response DTO로 변환하는 로직을 별도의 메서드로 작성하면 여러
기능에서 반복해서 사용할 수 있다.

``` java
private 응답DTO mapToResponse(회원엔티티 엔티티) {
    return 응답DTO.builder()
            .번호(엔티티.get번호())
            .이름(엔티티.get이름())
            .이메일(엔티티.get이메일())
            .나이(엔티티.get나이())
            .build();
}
```

#### 5. Stream을 이용한 여러 회원 등록 및 전체 조회

여러 개의 요청 데이터를 한 번에 처리하기 위해 `List`와 Stream을
사용하였다.

``` java
@Transactional
public List<응답DTO> createBatch(List<요청DTO> 요청목록) {
    return 요청목록.stream()
            .map(this::create)
            .toList();
}
```

전체 회원 조회에서도 Entity 목록을 Response DTO 목록으로 변환할 수 있다.

``` java
public List<응답DTO> findAll() {
    return repository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
}
```

#### 6. 회원 CRUD API 구현

Controller에서는 HTTP 요청을 받아 Service의 기능을 호출한다.

``` java
@PostMapping
public List<응답DTO> post(@RequestBody List<요청DTO> 요청목록) {
    return service.createBatch(요청목록);
}

@GetMapping
public List<응답DTO> get() {
    return service.findAll();
}

@GetMapping("/{id}")
public 회원엔티티 get(@PathVariable Long id) {
    return service.findById(id).orElse(null);
}

@PutMapping("/{id}")
public 회원엔티티 update(
        @PathVariable Long id,
        @RequestBody 회원엔티티 엔티티) {

    엔티티.set번호(id);
    return service.save(엔티티);
}

@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
    service.deleteById(id);
}
```

사용한 HTTP 메서드의 역할은 다음과 같다.

  HTTP 메서드   기능
  ------------- -------------
  `POST`        데이터 등록
  `GET`         데이터 조회
  `PUT`         데이터 수정
  `DELETE`      데이터 삭제

#### 7. 게시글 DTO 구성

게시글 등록과 수정에서는 제목과 내용을 Request DTO로 전달받는다.

``` java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 게시글요청DTO {
    private String 제목;
    private String 내용;
}
```

응답 DTO에는 게시글뿐만 아니라 작성 회원 정보와 생성·수정 시간을 포함할
수 있다.

``` java
@Data
@Builder
public class 게시글응답DTO {
    private Long 번호;
    private Long 회원번호;
    private String 회원이름;
    private String 이메일;
    private String 제목;
    private String 내용;
    private Date 생성일시;
    private Date 수정일시;
}
```

#### 8. 회원과 게시글의 연관관계

여러 게시글이 하나의 회원을 참조하는 구조이므로 `@ManyToOne`을 사용한다.

``` java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class 게시글엔티티 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 번호;

    private String 제목;
    private String 내용;

    @CreatedDate
    private Date 생성일시;

    @LastModifiedDate
    private Date 수정일시;

    @ManyToOne
    private 회원엔티티 회원;
}
```

-   `@ManyToOne` : 여러 게시글이 하나의 회원과 연결되는 다대일 관계를
    설정한다.
-   `@CreatedDate` : 데이터가 처음 생성된 시간을 자동 저장한다.
-   `@LastModifiedDate` : 데이터가 수정된 시간을 자동 갱신한다.
-   `@EntityListeners(AuditingEntityListener.class)` : JPA Auditing이
    Entity의 생성·수정 시간을 처리하도록 한다.

#### 9. 회원의 게시글 생성

게시글을 생성할 때 URL에서 회원 번호를 전달받아 해당 회원을 먼저 조회한
뒤 게시글과 연결한다.

``` java
public 게시글응답DTO create(Long 회원번호, 게시글요청DTO 요청) {

    회원엔티티 회원 = 회원Repository.findById(회원번호)
            .orElseThrow();

    게시글엔티티 게시글 = 게시글엔티티.builder()
            .제목(요청.get제목())
            .내용(요청.get내용())
            .회원(회원)
            .build();

    게시글Repository.save(게시글);

    return mapToResponse(게시글);
}
```

게시글 생성 API는 다음과 같이 구성한다.

``` java
@PostMapping("/{id}/articles")
@ResponseStatus(HttpStatus.CREATED)
public 게시글응답DTO postArticle(
        @PathVariable Long id,
        @RequestBody 게시글요청DTO 요청) {

    return 게시글Service.create(id, 요청);
}
```

정상적으로 생성되면 `201 Created` 상태를 확인할 수 있다.

#### 10. 게시글 전체 조회와 회원별 조회

전체 게시글은 `findAll()`을 이용하여 조회한 뒤 Response DTO로 변환한다.

``` java
public List<게시글응답DTO> findAll() {
    return 게시글Repository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
}
```

특정 회원의 게시글만 조회할 때는 회원 번호를 기준으로 필터링하였다.

``` java
public List<게시글응답DTO> findByMemberId(Long 회원번호) {
    return 게시글Repository.findAll()
            .stream()
            .filter(게시글 -> 게시글.get회원().get번호().equals(회원번호))
            .map(this::mapToResponse)
            .toList();
}
```

#### 11. 게시글 한 건 조회

게시글 번호를 이용하여 특정 게시글 하나를 조회한다.

``` java
public 게시글응답DTO findById(Long 게시글번호) {

    게시글엔티티 게시글 = 게시글Repository.findById(게시글번호)
            .orElseThrow();

    return mapToResponse(게시글);
}
```

Controller에서는 Path Variable로 게시글 번호를 전달받는다.

``` java
@GetMapping("/{id}")
public 게시글응답DTO getArticle(@PathVariable Long id) {
    return 게시글Service.findById(id);
}
```

#### 12. 게시글 수정

게시글 번호로 기존 데이터를 조회한 뒤 제목과 내용을 변경하고 저장한다.

``` java
public 게시글응답DTO update(Long 게시글번호, 게시글요청DTO 요청) {

    게시글엔티티 게시글 = 게시글Repository.findById(게시글번호)
            .orElseThrow();

    게시글.set제목(요청.get제목());
    게시글.set내용(요청.get내용());

    게시글Repository.save(게시글);

    return mapToResponse(게시글);
}
```

`@LastModifiedDate`가 적용되어 있으면 수정 시 `updated` 값도 변경된다.

#### 13. 게시글 조회 API 경로 구분

게시글 조회 조건에 따라 같은 `/articles` 경로를 서로 다르게 사용할 수
있다.

``` java
@GetMapping
public List<게시글응답DTO> getAllArticles() {
    return 게시글Service.findAll();
}

@GetMapping(params = "memberId")
public List<게시글응답DTO> getArticleByMemberId(
        @RequestParam("memberId") Long 회원번호) {

    return 게시글Service.findByMemberId(회원번호);
}

@GetMapping("/{id}")
public 게시글응답DTO getArticle(@PathVariable Long id) {
    return 게시글Service.findById(id);
}
```

호출 주소는 다음과 같이 구분된다.

``` text
GET /articles
GET /articles?memberId={회원번호}
GET /articles/{게시글번호}
```

#### 14. Postman을 이용한 API 테스트

구현한 API를 Postman에서 다음 순서로 테스트하였다.

``` text
POST /members
→ 회원 생성 및 회원 id 확인

POST /members/{id}/articles
→ 해당 회원의 게시글 생성
→ created / updated 자동 생성 확인

GET /articles
→ 전체 게시글 조회

GET /articles?memberId={id}
→ 특정 회원이 작성한 게시글 조회

GET /articles/{id}
→ 게시글 한 건 조회

PUT /articles/{id}
→ 게시글 제목과 내용 수정
→ updated 변경 확인
```

`POST /members/{id}/articles` 요청에서는 회원 데이터가 아닌 게시글
Request DTO에 맞는 JSON을 전달해야 한다.

``` json
{
  "title": "게시글 제목",
  "description": "게시글 내용"
}
```

#### 핵심 정리

-   DTO를 이용하여 요청 데이터와 응답 데이터를 분리할 수 있다.
-   Entity는 JPA를 통해 데이터베이스 테이블과 연결된다.
-   `JpaRepository`를 사용하면 기본적인 CRUD 기능을 간단하게 구현할 수
    있다.
-   Service는 비즈니스 로직과 DTO·Entity 변환을 담당한다.
-   Controller는 URL과 HTTP 메서드를 매핑하여 클라이언트 요청을
    처리한다.
-   `@ManyToOne`을 이용하여 회원과 게시글의 다대일 관계를 구성할 수
    있다.
-   `@CreatedDate`, `@LastModifiedDate`를 이용하여 생성일과 수정일을
    자동 관리할 수 있다.
-   `@PathVariable`은 URL 경로의 값을 받고, `@RequestParam`은 쿼리
    파라미터 값을 받을 때 사용한다.
-   같은 URL이라도 `GET`, `POST`, `PUT`, `DELETE` 등 HTTP 메서드에 따라
    서로 다른 기능을 수행한다.
-   Postman 테스트에서는 Controller에 정의한 HTTP 메서드와 URL, Request
    DTO의 JSON 구조를 정확하게 맞춰야 한다.

#### 가장 어려웠던 부분 - URL 매핑과 HTTP 메서드 구분

이번 실습에서 가장 어려웠던 부분은 회원과 게시글 API의 URL 구조를
구분하고, 각 URL에서 사용할 HTTP 메서드를 정확하게 맞추는 부분이었다.

특히 게시글 생성과 조회는 주소가 비슷하지만 서로 다른 Controller와 HTTP
메서드를 사용한다.

``` java
// 회원의 게시글 생성
@RestController
@RequestMapping("/members")
public class 회원Controller {

    @PostMapping("/{id}/articles")
    public 게시글응답DTO createArticle(
            @PathVariable Long id,
            @RequestBody 게시글요청DTO 요청) {

        return 게시글Service.create(id, 요청);
    }
}
```

``` java
// 게시글 조회 및 수정
@RestController
@RequestMapping("/articles")
public class 게시글Controller {

    @GetMapping
    public List<게시글응답DTO> getAllArticles() {
        return 게시글Service.findAll();
    }

    @GetMapping(params = "memberId")
    public List<게시글응답DTO> getByMemberId(
            @RequestParam Long memberId) {

        return 게시글Service.findByMemberId(memberId);
    }

    @GetMapping("/{id}")
    public 게시글응답DTO getArticle(@PathVariable Long id) {
        return 게시글Service.findById(id);
    }

    @PutMapping("/{id}")
    public 게시글응답DTO updateArticle(
            @PathVariable Long id,
            @RequestBody 게시글요청DTO 요청) {

        return 게시글Service.update(id, 요청);
    }
}
```

따라서 다음 두 요청은 서로 다른 기능이다.

```java
package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<ArticleResponse> getAllArticles(){
        return articleService.findAll();
    }

    @GetMapping(params = "memberId")
    public List<ArticleResponse> getArticleByMemberId(
            @RequestParam("memberId") Long memberId){
        return articleService.findByMemberId(memberId);
    }
    // 게시글 한 건 조회
    @GetMapping("/{id}")
    public ArticleResponse getArticle(
            @PathVariable("id") Long id) {

        return articleService.findById(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ArticleResponse updateArticle(
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest) {

        return articleService.update(id, articleRequest);
    }
}

```

또한 `POST /members/1/articles`에서 회원 생성용 JSON을 보내면 Request
DTO의 구조가 맞지 않아 `400 Bad Request`가 발생할 수 있고, POST만 정의된
`/members/1/articles`에 GET 요청을 보내면 `405 Method Not Allowed`가
발생할 수 있다는 점을 실습을 통해 확인하였다.

**느낀점**

처음에는 URL만 맞으면 요청이 실행되는 것으로 생각했지만, 실제 REST
API에서는 URL뿐만 아니라 HTTP 메서드와 요청 Body의 데이터 구조까지 모두
일치해야 한다는 것을 알게 되었다. 특히 `400 Bad Request`와
`405 Method Not Allowed` 오류를 직접 확인하면서 단순히 오류 메시지만
보는 것이 아니라 Controller의 `@RequestMapping`, `@GetMapping`,
`@PostMapping`, `@PutMapping`과 Request DTO를 함께 확인해야 한다는 점이
기억에 남았다. 앞으로 API 오류가 발생하면 URL, HTTP 메서드, 요청 JSON,
Controller 매핑 순서로 확인하면 문제를 더 빠르게 찾을 수 있을 것 같다.
