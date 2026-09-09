# 39일차

## Spring Boot 회원 관리 및 Spring Security

📌 학습일 : 2026.09.09

📌 학습 내용 : Spring Data JPA, Thymeleaf, 회원 등록·수정, Controller와 RestController, Spring Security, BCrypt, 권한 설정

## 회원 엔티티와 Repository 구성

```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 엔티티클래스명 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 아이디;
    private String 이름;
    private String 이메일;
    private Integer 나이;
}
```

`@Entity`를 사용해 클래스를 데이터베이스 테이블과 연결하고, `@Id`와 `@GeneratedValue`를 이용해 기본키가 자동으로 생성되도록 설정하였다.

```java
public interface 저장소인터페이스명
        extends JpaRepository<엔티티클래스명, Long> {
}
```

`JpaRepository`를 상속받아 별도의 SQL문을 직접 작성하지 않고 회원 저장, 조회, 수정, 삭제 기능을 사용할 수 있도록 하였다.

## 회원 등록

```java
@GetMapping("/회원/추가")
public String 회원추가화면() {
    return "회원-폼";
}

@PostMapping("/회원/추가")
public String 회원추가(엔티티클래스명 회원) {
    저장소객체.save(회원);
    return "redirect:/홈";
}
```

GET 요청으로 회원 등록 화면을 보여주고, POST 요청으로 입력받은 회원 정보를 `save()`를 이용해 저장하였다.

```html
<form th:action="@{/회원/추가}" method="post">
    이름: <input type="text" name="이름"><br>
    이메일: <input type="text" name="이메일"><br>
    나이: <input type="text" name="나이"><br>
    <button>가입자 생성</button>
</form>
```

Thymeleaf의 `th:action`을 이용하여 폼 데이터를 Controller로 전달하였다.

## 회원 수정

```java
@GetMapping("/회원/수정")
public String 회원수정화면(
        @RequestParam("아이디") Long 아이디,
        Model 모델) {

    엔티티클래스명 회원 =
            저장소객체.findById(아이디).orElseThrow();

    모델.addAttribute("회원", 회원);
    return "회원-수정-폼";
}
```

수정할 회원의 ID를 전달받아 `findById()`로 기존 정보를 조회하고 `Model`에 담아 수정 화면으로 전달하였다.

```html
<form th:action="@{/회원/수정}" method="post">
    <input type="hidden" name="아이디" th:value="${회원.아이디}">
    이름: <input type="text" name="이름" th:value="${회원.이름}"><br>
    이메일: <input type="text" name="이메일" th:value="${회원.이메일}"><br>
    나이: <input type="text" name="나이" th:value="${회원.나이}"><br>
    <button>가입자 수정</button>
</form>
```

`th:value`로 기존 회원 정보를 입력창에 표시하고, ID는 `hidden`으로 함께 전달하였다.

```java
@PostMapping("/회원/수정")
public String 회원수정(엔티티클래스명 회원) {
    저장소객체.save(회원);
    return "redirect:/홈";
}
```

수정된 회원 객체를 다시 `save()`하여 변경된 정보를 저장하였다.

## 회원 목록과 Thymeleaf

```java
@GetMapping("/회원/목록")
public String 회원목록(Model 모델) {
    모델.addAttribute("회원목록", 회원목록);
    return "회원-목록";
}
```

Controller에서 회원 목록을 `Model`에 담아 HTML로 전달하였다.

```html
<tr th:each="회원 : ${회원목록}">
    <td th:text="${회원.아이디}"></td>
    <td th:text="${회원.이름}"></td>
    <td th:text="${회원.이메일}"></td>
</tr>
```

`th:each`로 회원 목록을 반복하고 `th:text`를 이용해 각 회원의 정보를 출력하였다.

## Controller와 RestController

```java
@Controller
public class 컨트롤러클래스명 {

    @GetMapping("/회원/목록")
    public String 회원목록(Model 모델) {
        return "회원-목록";
    }
}
```

`@Controller`를 이용해 HTML 화면을 반환하는 방식을 실습하였다.

```java
@RestController
public class API컨트롤러클래스명 {

    @GetMapping("/api/회원")
    public List<엔티티클래스명> 회원목록() {
        return 회원목록;
    }
}
```

`@RestController`에서는 객체나 리스트를 직접 응답 데이터로 반환하는 방식을 확인하였다.

## Spring Security 기본 설정

```properties
spring.security.user.name=사용자명
spring.security.user.password=비밀번호
spring.security.user.role=USER,ADMIN
```

Spring Security에서 사용할 기본 사용자 이름, 비밀번호, 권한을 설정하였다.

## 회원 비밀번호와 권한

```java
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class 엔티티클래스명 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 아이디;
    private String 이름;
    private String 이메일;
    private Integer 나이;
    private String 비밀번호;
    private String 권한;
}
```

회원 정보에 로그인에 필요한 비밀번호와 사용자의 접근 권한을 저장할 필드를 추가하였다.

## 이메일을 이용한 회원 조회

```java
public interface 저장소인터페이스명
        extends JpaRepository<엔티티클래스명, Long> {

    Optional<엔티티클래스명> findByEmail(String 이메일);
}
```

로그인할 회원을 이메일로 찾을 수 있도록 `findByEmail()`을 작성하였다. `Optional`을 사용하여 회원이 존재하지 않는 경우도 처리할 수 있도록 하였다.

## BCrypt 비밀번호 암호화

```java
var 비밀번호암호화객체 = new BCryptPasswordEncoder();

저장소객체.save(
        엔티티클래스명.builder()
                .name("사용자이름")
                .email("사용자이메일")
                .age(나이)
                .password(비밀번호암호화객체.encode("비밀번호"))
                .authority("ROLE_USER")
                .build()
);
```

`BCryptPasswordEncoder`를 이용해 비밀번호를 암호화한 뒤 데이터베이스에 저장하였다.

일반 사용자에게는 `ROLE_USER`, 관리자에게는 `ROLE_ADMIN` 권한을 지정하였다.

## UserDetailsService

```java
@Bean
public UserDetailsService 사용자정보서비스(
        저장소인터페이스명 저장소객체) {

    return username -> {

        var 회원 = 저장소객체
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

로그인할 때 입력한 사용자 정보를 이용해 데이터베이스에서 회원을 조회하고, 조회된 비밀번호와 권한 정보를 Spring Security에서 사용할 수 있도록 연결하는 과정을 실습하였다.

## PasswordEncoder

```java
@Bean
public PasswordEncoder 비밀번호암호화() {
    return new BCryptPasswordEncoder();
}
```

`BCryptPasswordEncoder`를 Bean으로 등록하여 Spring Security에서 비밀번호를 처리할 수 있도록 하였다.

## 페이지 접근 권한

```java
http.authorizeHttpRequests(권한 -> 권한
        .requestMatchers("/", "/홈").permitAll()
        .requestMatchers("/회원/**").hasAnyAuthority("ROLE_ADMIN")
        .anyRequest().authenticated()
);
```

URL에 따라 접근할 수 있는 사용자의 권한을 다르게 설정하였다.

- `permitAll()` : 누구나 접근 가능
- `hasAnyAuthority()` : 지정된 권한을 가진 사용자만 접근 가능
- `authenticated()` : 로그인한 사용자만 접근 가능

회원 관련 페이지는 관리자 권한을 가진 사용자만 접근하도록 설정하였다.

## 로그인과 로그아웃

```java
.formLogin(Customizer.withDefaults())
.logout(Customizer.withDefaults());
```

Spring Security에서 제공하는 기본 로그인 화면과 로그아웃 기능을 적용하였다.

## 핵심 정리

- JPA를 이용해 회원 정보를 저장하고 조회하였다.
- GET과 POST 요청을 이용해 회원 등록과 수정 기능을 구현하였다.
- `Model`을 통해 Controller의 데이터를 Thymeleaf로 전달하였다.
- `th:each`, `th:text`, `th:value`를 이용해 데이터를 화면에 출력하였다.
- `@Controller`와 `@RestController`의 차이를 실습하였다.
- 회원 정보에 비밀번호와 권한을 추가하였다.
- 이메일을 이용해 로그인할 회원을 조회하였다.
- BCrypt를 이용해 비밀번호를 암호화하였다.
- Spring Security에서 사용자 정보와 데이터베이스의 회원 정보를 연결하였다.
- 일반 사용자와 관리자에 따라 페이지 접근 권한을 다르게 설정하였다.
- 기본 로그인과 로그아웃 기능을 적용하였다.

## 배운점

회원 정보를 단순히 저장하고 조회하는 것에서 끝나는 것이 아니라 Controller와 Thymeleaf를 연결하여 실제 화면에서 회원을 등록하고 수정하는 흐름을 실습하였다. 또한 `@Controller`와 `@RestController`가 결과를 반환하는 방식이 어떻게 다른지도 확인할 수 있었다.

Spring Security에서는 비밀번호를 그대로 저장하지 않고 BCrypt를 이용해 암호화하며, 회원마다 권한을 지정하여 접근할 수 있는 페이지를 다르게 설정하는 방법을 배웠다. 로그인할 때 입력한 정보를 이용해 데이터베이스에서 회원을 조회하고 인증에 사용하는 흐름도 함께 확인하였다.

## 느낀점

회원 등록과 수정 기능까지는 Controller에서 데이터를 받아 Repository에 저장하는 흐름이 비교적 단순했지만, Spring Security가 추가되면서 비밀번호 암호화와 사용자 조회, 권한 설정까지 연결해야 해서 코드의 흐름이 조금 더 복잡하게 느껴졌다.

특히 일반 사용자와 관리자에게 서로 다른 권한을 주고 페이지마다 접근 범위를 설정하는 부분이 처음에는 헷갈렸지만, 실제 웹 서비스에서 로그인한 사용자에 따라 접근할 수 있는 기능이 달라지는 원리를 조금씩 이해할 수 있었다.
