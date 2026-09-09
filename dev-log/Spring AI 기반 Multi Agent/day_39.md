# 39일차

## Spring Boot 회원 관리 및 Spring Security

📌 학습일 : 2026.09.09

📌 학습 내용 : Spring Data JPA, Thymeleaf, 회원 관리, Spring Security,
BCrypt, 권한 설정

---

#### 1. 회원 Entity와 Repository 구성

``` java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 회원엔티티 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 번호;
    private String 이름;
    private String 이메일;
    private Integer 나이;
}
```

-   `@Entity` : JPA가 관리하는 Entity로 지정한다.
-   `@Id` : 기본키를 지정한다.
-   `@GeneratedValue` : 기본키 값을 자동 생성한다.

``` java
public interface 회원Repository
        extends JpaRepository<회원엔티티, Long> {
}
```

`JpaRepository`를 상속하여 별도의 SQL문을 직접 작성하지 않고 회원
데이터를 저장, 조회, 수정, 삭제할 수 있도록 구성하였다.

#### 2. 회원 등록

``` java
@GetMapping("/member/add")
public String 회원등록화면(){
    return "member-form";
}

@PostMapping("/member/add")
public String 회원등록(회원엔티티 회원){
    회원Repository.save(회원);
    return "redirect:/home";
}
```

`GET` 요청으로 회원 등록 화면을 불러오고, 입력한 회원 정보는 `POST`
요청으로 전달받아 `save()`를 이용해 저장하였다.

``` html
<form th:action="@{/member/add}" method="post">
    이름: <input type="text" name="name"><br>
    이메일: <input type="text" name="email"><br>
    나이: <input type="text" name="age"><br>
    <button>가입자 생성</button>
</form>
```

Thymeleaf의 `th:action`을 이용하여 입력한 회원 정보를 Controller로
전달하였다.

#### 3. 회원 수정

``` java
@GetMapping("/member/update")
public String 회원수정화면(
        @RequestParam("id") Long 번호,
        Model 모델) {

    회원엔티티 회원 =
            회원Repository.findById(번호).orElseThrow();

    모델.addAttribute("member", 회원);

    return "member-update-form";
}
```

수정할 회원의 번호를 전달받아 `findById()`로 기존 정보를 조회한 뒤
`Model`에 담아 수정 화면으로 전달하였다.

``` html
<form th:action="@{/member/update}" method="post">

    <input type="hidden"
           name="id"
           th:value="${member.id}">

    이름:
    <input type="text"
           name="name"
           th:value="${member.name}"><br>

    이메일:
    <input type="text"
           name="email"
           th:value="${member.email}"><br>

    나이:
    <input type="text"
           name="age"
           th:value="${member.age}"><br>

    <button>가입자 수정</button>
</form>
```

`th:value`를 이용해 기존 회원 정보를 입력창에 표시하고, 회원 번호는
`hidden`으로 함께 전달하였다.

``` java
@PostMapping("/member/update")
public String 회원수정(회원엔티티 회원){
    회원Repository.save(회원);
    return "redirect:/home";
}
```

기존 회원의 번호가 포함된 객체를 다시 `save()`하여 회원 정보를
수정하였다.

#### 4. 회원 목록과 Thymeleaf

``` java
@GetMapping("/members")
public String 회원목록(Model 모델) {

    모델.addAttribute("members", 회원목록);

    return "members";
}
```

Controller에서 회원 목록을 `Model`에 담아 HTML로 전달하였다.

``` html
<tr th:each="member : ${members}">
    <td th:text="${member.id}"></td>
    <td th:text="${member.name}"></td>
    <td th:text="${member.email}"></td>
</tr>
```

-   `th:each` : 회원 목록을 하나씩 반복한다.
-   `th:text` : 회원 객체의 값을 HTML 화면에 출력한다.
-   `Model` : Controller의 데이터를 HTML 화면으로 전달한다.

#### 5. Controller와 RestController

``` java
@Controller
public class 화면Controller {

    @GetMapping("/members")
    public String 회원목록(Model 모델){
        return "members";
    }
}
```

`@Controller`는 주로 HTML과 같은 화면을 반환할 때 사용한다.

``` java
@RestController
public class APIController {

    @GetMapping("/api/members")
    public List<회원엔티티> 회원목록(){
        return 회원목록;
    }
}
```

`@RestController`는 객체나 리스트 등의 데이터를 HTTP 응답으로 직접
반환할 때 사용한다.

#### 6. Spring Security 기본 사용자 설정

``` properties
spring.security.user.name=사용자명
spring.security.user.password=비밀번호
spring.security.user.role=USER,ADMIN
```

`application.properties`에서 Spring Security의 기본 사용자 이름,
비밀번호와 권한을 설정하였다.

#### 7. 회원 비밀번호와 권한 추가

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
    private String 이메일;
    private Integer 나이;
    private String 비밀번호;
    private String 권한;
}
```

회원 정보에 로그인 인증에 필요한 비밀번호와 사용자의 접근 권한을 저장할
필드를 추가하였다.

일반 사용자는 `ROLE_USER`, 관리자는 `ROLE_ADMIN` 권한을 사용하도록
설정하였다.

#### 8. 이메일을 이용한 회원 조회

``` java
public interface 회원Repository
        extends JpaRepository<회원엔티티, Long> {

    Optional<회원엔티티> findByEmail(String 이메일);
}
```

Spring Data JPA의 쿼리 메서드를 이용하여 이메일을 기준으로 회원을
조회하도록 하였다.

`Optional`을 사용하여 해당 이메일을 가진 회원이 존재하지 않는 경우도
처리할 수 있도록 구성하였다.

#### 9. BCrypt를 이용한 비밀번호 암호화

``` java
var 비밀번호암호화 = new BCryptPasswordEncoder();

회원Repository.save(
        회원엔티티.builder()
                .name("회원명")
                .email("member@test.com")
                .age(20)
                .password(비밀번호암호화.encode("1234"))
                .authority("ROLE_USER")
                .build()
);
```

비밀번호를 그대로 데이터베이스에 저장하지 않고 `BCryptPasswordEncoder`의
`encode()`를 이용해 암호화한 뒤 저장하였다.

#### 10. UserDetailsService 설정

``` java
@Bean
public UserDetailsService 사용자정보서비스(
        회원Repository 회원Repository) {

    return username -> {

        var 회원 = 회원Repository
                .findByEmail(username)
                .orElseThrow();

        return User.builder()
                .username(회원.getEmail())
                .password(회원.getPassword())
                .authorities(회원.getAuthority())
                .build();
    };
}
```

로그인할 때 입력한 이메일을 기준으로 데이터베이스에서 회원 정보를
조회하고, 조회한 이메일, 비밀번호, 권한 정보를 Spring Security에서
사용할 수 있도록 연결하였다.

#### 11. PasswordEncoder 등록

``` java
@Bean
public PasswordEncoder 비밀번호암호화(){
    return new BCryptPasswordEncoder();
}
```

`BCryptPasswordEncoder`를 Bean으로 등록하여 Spring Security에서
비밀번호를 처리할 수 있도록 하였다.

#### 12. 페이지 접근 권한 설정

``` java
http.authorizeHttpRequests(권한 -> 권한
        .requestMatchers("/", "/home").permitAll()
        .requestMatchers("/member/**")
        .hasAnyAuthority("ROLE_ADMIN")
        .anyRequest().authenticated()
);
```

-   `permitAll()` : 로그인하지 않아도 접근할 수 있다.
-   `hasAnyAuthority("ROLE_ADMIN")` : 관리자 권한을 가진 사용자만 접근할
    수 있다.
-   `authenticated()` : 로그인한 사용자만 접근할 수 있다.

URL에 따라 일반 사용자와 관리자가 접근할 수 있는 페이지를 구분하였다.

#### 13. 로그인과 로그아웃

``` java
.formLogin(Customizer.withDefaults())
.logout(Customizer.withDefaults());
```

Spring Security에서 제공하는 기본 로그인 화면과 로그아웃 기능을
적용하였다.

---

#### 핵심 정리

-   Spring Data JPA를 이용하여 회원 데이터를 저장하고 조회하였다.
-   `GET`, `POST` 요청을 이용하여 회원 등록과 수정 기능을 구현하였다.
-   `Model`을 이용하여 Controller의 데이터를 Thymeleaf 화면으로
    전달하였다.
-   `th:each`, `th:text`, `th:value`를 이용하여 회원 정보를 화면에
    표시하였다.
-   `@Controller`와 `@RestController`의 차이를 확인하였다.
-   회원 정보에 로그인에 필요한 비밀번호와 권한을 추가하였다.
-   이메일을 기준으로 로그인할 회원 정보를 조회하였다.
-   BCrypt를 이용하여 비밀번호를 암호화하였다.
-   `UserDetailsService`를 이용하여 데이터베이스의 회원 정보와 Spring
    Security를 연결하였다.
-   `ROLE_USER`, `ROLE_ADMIN`을 이용하여 사용자별 접근 권한을
    설정하였다.
-   Spring Security의 기본 로그인과 로그아웃 기능을 적용하였다.

---


Postman의 Authorization에서 Basic Auth를 설정하고 Username과 Password를 입력하여 인증이 필요한 API를 호출하는 방법을 배웠다. 인증 정보가 올바르면 GET /api/members 요청에 대해 200 OK가 반환되고 회원 데이터를 정상적으로 조회할 수 있다는 것을 확인하였다.
