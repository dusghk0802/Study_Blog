# 40일차

## Spring Boot 회원·게시글 관리 및 Spring Security

📌 학습일 : 2026.09.10

📌 학습 내용 : Profile, Spring Data JPA, 회원·게시글 관리, DTO,
Validation, Spring Security, 비밀번호 암호화, Pagination

---

#### 1. Profile을 이용한 개발·테스트 환경 분리

``` properties
spring.datasource.url=jdbc:h2:mem:데이터베이스명
spring.datasource.username=사용자명
spring.datasource.password=비밀번호
```

개발 환경과 테스트 환경에서 서로 다른 데이터베이스 설정을 사용할 수
있도록 구성하였다.

``` java
@Component
@Profile("dev")
public class 초기데이터클래스 implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        저장소객체.save(엔티티클래스명.builder()
                .name("이름")
                .email("이메일")
                .age(나이)
                .build());
    }
}
```

`@Profile("dev")`를 사용하여 개발 환경에서만 초기 데이터가 등록되도록
설정하였다.

#### 2. 회원·게시글·권한 Entity 구성

``` java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 회원엔티티 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 아이디;

    private String 이름;
    private String 이메일;
    private Integer 나이;
    private String 비밀번호;
}
```

회원 정보를 Entity로 구성하고 로그인에 사용할 비밀번호를 추가하였다.

``` java
@ManyToOne
@JoinColumn(name = "회원_아이디")
private 회원엔티티 회원;
```

게시글과 권한 Entity에 `@ManyToOne`을 사용하여 회원과 연관관계를
설정하였다.

#### 3. Repository 구성

``` java
public interface 회원저장소
        extends JpaRepository<회원엔티티, Long> {

    Optional<회원엔티티> findByEmail(String 이메일);
}
```

`JpaRepository`를 상속하여 기본적인 CRUD 기능을 사용하고,
`findByEmail()`을 이용해 이메일로 회원을 조회하도록 하였다.

``` java
public interface 게시글저장소
        extends JpaRepository<게시글엔티티, Long> {

    @Transactional
    void deleteAllByMember(회원엔티티 회원);
}
```

회원 삭제 시 해당 회원이 작성한 게시글을 함께 삭제할 수 있도록 메서드를
작성하였다.

#### 4. UserDetails를 이용한 로그인 사용자 정보 구성

``` java
public class 사용자정보클래스 implements UserDetails {

    private String username;
    private String password;
    private List<? extends GrantedAuthority> authorities;

    private String 표시이름;
    private Long 회원아이디;
}
```

`UserDetails`를 구현하여 데이터베이스에 저장된 회원 정보를 Spring
Security에서 사용할 수 있도록 구성하였다.

``` java
this.username = 회원.getEmail();
this.displayName = 회원.getName();
this.password = 회원.getPassword();
this.memberId = 회원.getId();

this.authorities = 권한목록.stream()
        .map(권한 -> new SimpleGrantedAuthority(권한.getAuthority()))
        .toList();
```

회원의 이메일을 로그인 아이디로 사용하고 회원 ID와 이름, 비밀번호, 권한
정보를 함께 관리하였다.

#### 5. DTO와 Form 분리

``` java
@Data
@Builder
public class 회원DTO {

    private Long 아이디;
    private String 이름;
    private String 이메일;
}
```

회원 Entity를 화면에 직접 전달하지 않고 필요한 정보만 DTO로 변환하여
사용하였다.

``` java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class 회원폼 {

    private Long 아이디;

    @NotBlank(message = "이름을 입력하세요")
    private String 이름;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 잘못되었습니다")
    private String 이메일;

    private String 비밀번호;
    private String 비밀번호확인;
}
```

사용자가 입력하는 값은 Form 객체로 받아 Entity, DTO, Form의 역할을
구분하였다.

#### 6. Service에서 Entity와 DTO 변환

``` java
private 회원DTO DTO변환(회원엔티티 회원) {

    return 회원DTO.builder()
            .id(회원.getId())
            .name(회원.getName())
            .email(회원.getEmail())
            .build();
}
```

Service에서 Entity를 DTO로 변환하여 Controller에 필요한 데이터만
전달하도록 구성하였다.

게시글도 동일하게 게시글 Entity를 게시글 DTO로 변환하여 ID, 제목, 내용,
작성자, 작성일 등의 정보를 전달하였다.

#### 7. 회원가입 기능

``` java
@PostMapping("/signup")
public String 회원가입(
        @Valid @ModelAttribute("회원") 회원폼 회원폼,
        BindingResult 검증결과) {

    if (검증결과.hasErrors()) {
        return "회원가입";
    }

    회원서비스.create(회원폼);

    return "redirect:/";
}
```

회원가입 Form에서 입력한 값을 `@Valid`와 `BindingResult`를 이용해 검증한
뒤 문제가 없는 경우 회원 정보를 저장하였다.

#### 8. 회원가입 입력값 검증

``` java
if (회원폼.getPassword() == null ||
        회원폼.getPassword().trim().length() < 8) {

    검증결과.rejectValue(
            "비밀번호",
            "오류코드",
            "비밀번호를 8글자 이상 입력하세요"
    );
}
```

비밀번호가 8글자 이상인지 확인하고 비밀번호와 비밀번호 확인 값이 같은지
검사하였다.

``` java
if (회원서비스.findByEmail(
        회원폼.getEmail()).isPresent()) {

    검증결과.rejectValue(
            "이메일",
            "오류코드",
            "사용 중인 이메일입니다"
    );
}
```

이미 등록된 이메일인지 확인하여 중복 회원가입을 방지하였다.

#### 9. BCrypt를 이용한 비밀번호 암호화

``` java
회원엔티티 회원 = 회원엔티티.builder()
        .name(회원폼.getName())
        .password(비밀번호암호화객체.encode(
                회원폼.getPassword()))
        .email(회원폼.getEmail())
        .build();
```

회원가입 시 비밀번호를 그대로 저장하지 않고 `PasswordEncoder`의
`encode()`를 이용해 암호화하여 저장하였다.

``` java
비밀번호암호화객체.matches(
        입력비밀번호,
        회원.getPassword()
);
```

비밀번호 확인 시에는 `matches()`를 사용하여 입력한 비밀번호와 암호화된
비밀번호가 일치하는지 확인하였다.

#### 10. 비밀번호 변경

``` java
@Data
public class 비밀번호폼 {

    @NotBlank(message = "기존 비밀번호를 입력해 주세요")
    private String 기존비밀번호;

    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    @NotBlank(message = "새로운 비밀번호를 입력해 주세요")
    private String 새비밀번호;

    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    @NotBlank(message = "새로운 비밀번호를 입력해 주세요")
    private String 비밀번호확인;
}
```

기존 비밀번호를 확인한 뒤 새로운 비밀번호와 비밀번호 확인 값이 일치하는
경우 새 비밀번호를 암호화하여 저장하였다.

#### 11. 회원 목록 조회 및 수정

``` java
public Page<회원DTO> 전체조회(Pageable 페이지정보) {

    return 회원저장소.findAll(페이지정보)
            .map(this::DTO변환);
}
```

회원 목록을 `Page` 형태로 조회하여 페이징 기능을 적용하였다.

``` java
if (회원폼.getName() != null) {
    회원.setName(회원폼.getName());
}

if (회원폼.getEmail() != null) {
    회원.setEmail(회원폼.getEmail());
}

회원저장소.save(회원);
```

수정할 회원을 조회한 뒤 입력된 값만 변경하고 다시 저장하였다.

#### 12. 회원 삭제

``` java
회원엔티티 회원 = 회원저장소
        .findById(아이디)
        .orElseThrow();

게시글저장소.deleteAllByMember(회원);
회원저장소.delete(회원);
```

회원을 삭제할 때 회원이 작성한 게시글을 먼저 삭제한 후 회원 정보를
삭제하였다.

#### 13. 게시글 CRUD

``` java
public 게시글DTO 등록(
        Long 회원아이디,
        게시글폼 게시글폼) {

    회원엔티티 회원 = 회원저장소
            .findById(회원아이디)
            .orElseThrow();

    게시글엔티티 게시글 = 게시글엔티티.builder()
            .title(게시글폼.getTitle())
            .description(게시글폼.getDescription())
            .member(회원)
            .build();

    게시글저장소.save(게시글);

    return DTO변환(게시글);
}
```

게시글 등록 시 회원 ID로 작성자를 조회한 뒤 게시글과 회원을 연결하여
저장하였다.

게시글의 전체 조회, 상세 조회, 등록, 수정, 삭제 기능을 Service와
Controller로 나누어 구현하였다.

#### 14. 로그인한 사용자와 게시글 작성자 연결

``` java
@AuthenticationPrincipal
사용자정보클래스 사용자정보
```

`@AuthenticationPrincipal`을 이용하여 현재 로그인한 사용자의 정보를
가져왔다.

``` java
게시글서비스.create(
        사용자정보.getMemberId(),
        게시글폼
);
```

로그인한 회원의 ID를 게시글 등록에 전달하여 실제 로그인 사용자가 게시글
작성자로 저장되도록 하였다.

#### 15. Validation과 BindingResult

``` java
@NotBlank(message = "게시글 제목을 입력하세요")
private String 제목;

@NotBlank(message = "게시글 내용을 입력하세요")
private String 내용;
```

`@NotBlank`를 이용하여 제목이나 내용이 비어 있는 게시글이 등록되지
않도록 검증하였다.

``` java
if (검증결과.hasErrors()) {
    return "게시글-등록";
}
```

입력값에 문제가 있는 경우 저장하지 않고 다시 입력 화면으로 이동하도록
처리하였다.

#### 16. rejectValue()를 이용한 직접 검증

``` java
if (게시글폼.getTitle() != null &&
        게시글폼.getTitle().contains("특정문자")) {

    검증결과.rejectValue(
            "제목",
            "오류코드",
            "오류 메시지"
    );
}
```

Validation 어노테이션 외에도 특정 조건을 직접 검사하고 `rejectValue()`를
이용해 원하는 필드에 오류 메시지를 추가하는 방법을 실습하였다.

#### 17. Pagination을 이용한 목록 조회

``` java
@PageableDefault(
        size = 10,
        sort = "id",
        direction = Sort.Direction.DESC
)
Pageable 페이지정보
```

`Pageable`을 이용하여 회원과 게시글 목록을 한 번에 모두 조회하지 않고
페이지 단위로 조회하였다.

한 페이지에 10개의 데이터를 표시하고 ID를 기준으로 내림차순 정렬하여
최신 데이터가 먼저 나오도록 설정하였다.

#### 18. Controller와 Service 역할 분리

Controller에서는 URL 요청을 처리하고 Form 데이터 검증, 로그인 사용자
정보 확인, 화면 이동을 담당하도록 구성하였다.

Service에서는 회원과 게시글의 조회, 등록, 수정, 삭제와 비밀번호 암호화,
DTO 변환 등의 실제 처리 로직을 담당하도록 구성하였다.

이를 통해 Controller에 모든 기능을 작성하지 않고 역할에 따라 코드를
분리하는 방법을 실습하였다.

---

## 핵심 정리

-   Profile을 이용하여 개발 환경과 테스트 환경의 설정을 분리하였다.
-   JPA 연관관계를 이용하여 회원, 게시글, 권한 정보를 연결하였다.
-   `UserDetails`를 구현하여 DB의 회원 정보를 Spring Security에서
    사용하였다.
-   Entity, DTO, Form의 역할을 나누어 데이터를 처리하였다.
-   Controller와 Service의 역할을 나누어 회원과 게시글 기능을
    구현하였다.
-   회원가입 시 비밀번호 길이, 비밀번호 확인, 이메일 중복 여부를
    검사하였다.
-   BCrypt를 이용하여 비밀번호를 암호화하고 `matches()`로 기존
    비밀번호를 확인하였다.
-   회원 정보 수정과 비밀번호 변경 기능을 구현하였다.
-   회원 삭제 시 해당 회원이 작성한 게시글을 먼저 삭제하도록 처리하였다.
-   게시글의 조회, 등록, 수정, 삭제 기능을 구현하였다.
-   `@AuthenticationPrincipal`을 이용하여 로그인한 회원 정보를 가져왔다.
-   Validation과 `BindingResult`를 이용하여 입력값을 검증하였다.
-   `rejectValue()`를 이용하여 직접 검증 오류를 추가하였다.
-   `Pageable`과 `Page`를 이용하여 회원과 게시글 목록에 페이징을
    적용하였다.

---

training/Spring-Boot/2026.09.10/day_40_1.PNG


