# 73일차

## Spring Boot REST API로 Todo CRUD 만들기

📌 학습일 : 2026.10.24

📌 학습 내용 : REST API, @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PathVariable, @RequestBody, ResponseEntity, HTTP 상태 코드, Controller-Service 계층 분리, 생성자 주입, @RequiredArgsConstructor, Lombok, Optional, JUnit 테스트

---

#### 1. 프로젝트 구조

```
todo
├── build.gradle
└── src
    ├── main
    │   ├── java/com/example/demo
    │   │   ├── DemoApplication.java
    │   │   ├── controller/TodoController.java
    │   │   ├── model/Todo.java
    │   │   └── service/TodoService.java
    │   └── resources/application.properties
    └── test/java/com/example/demo/TodoServiceTests.java
```

| 계층 | 클래스 | 역할 |
| --- | --- | --- |
| Model | `Todo` | 할 일 데이터(번호, 제목, 완료 여부)를 담는 객체 |
| Service | `TodoService` | 저장·조회·수정·삭제 같은 실제 처리 담당 |
| Controller | `TodoController` | HTTP 요청을 받아 Service를 호출하고 응답 반환 |

이전 MVC 실습에서는 `@Controller`로 Thymeleaf 화면(HTML)을 반환했지만, 이번에는 `@RestController`로 화면 없이 JSON 데이터만 주고받는 API를 만들었다.

```gradle
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-webmvc'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
}
```

화면을 쓰지 않기 때문에 Thymeleaf 의존성은 빼고 Web MVC와 Lombok만 사용했다.

#### 2. REST API 설계

| 기능 | HTTP 메서드 | URL | 성공 시 응답 |
| --- | --- | --- | --- |
| 전체 조회 | GET | `/api/todos` | 200 OK |
| 단건 조회 | GET | `/api/todos/{id}` | 200 OK (없으면 404) |
| 등록 | POST | `/api/todos` | 201 Created |
| 수정 | PUT | `/api/todos/{id}` | 200 OK (없으면 404) |
| 삭제 | DELETE | `/api/todos/{id}` | 204 No Content (없으면 404) |

REST는 URL로 **자원(todos)**을 나타내고, 무엇을 할지는 **HTTP 메서드**로 구분한다. 그래서 `/getTodo`, `/deleteTodo`처럼 URL에 동작을 넣지 않고 같은 `/api/todos/{id}`에 GET, PUT, DELETE를 나눠 사용한다.

#### 3. Model - Lombok 활용

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Long id;
    private String title;
    private boolean done;
}
```

| 어노테이션 | 역할 |
| --- | --- |
| `@Data` | getter, setter, toString, equals, hashCode 자동 생성 |
| `@Builder` | `Todo.builder().title("...").build()` 형태로 객체 생성 |
| `@NoArgsConstructor` | 기본 생성자 생성 (JSON → 객체 변환 시 필요) |
| `@AllArgsConstructor` | 모든 필드를 받는 생성자 생성 (`@Builder`가 사용) |

`@Builder`만 붙이면 기본 생성자가 없어져서, 요청 JSON을 객체로 바꿀 때 문제가 생길 수 있어 `@NoArgsConstructor`와 `@AllArgsConstructor`를 함께 붙였다.

#### 4. Service - 메모리 저장소

```java
@Service
public class TodoService {
    private final Map<Long, Todo> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Todo save(Todo todo) {
        todo.setId(sequence.incrementAndGet());
        store.put(todo.getId(), todo);
        return todo;
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
```

DB 대신 `Map`에 저장해서 서버를 재시작하면 데이터가 사라진다. `LinkedHashMap`을 써서 등록한 순서대로 조회되도록 했다.

번호는 `AtomicLong`의 `incrementAndGet()`으로 1씩 늘려 부여했다. 일반 `long`에 `++`를 하면 여러 요청이 동시에 들어올 때 같은 번호가 나올 수 있지만, `AtomicLong`은 여러 스레드에서 동시에 써도 값이 안전하게 증가한다.

조회 결과가 없을 수도 있어서 `null` 대신 `Optional`로 감싸 반환했다. 호출하는 쪽에서 값이 없는 경우를 반드시 처리하게 되어 `NullPointerException`을 줄일 수 있다.

#### 5. Controller - 요청 처리와 응답 코드

```java
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.save(todo));
    }
}
```

| 어노테이션 | 역할 |
| --- | --- |
| `@RestController` | `@Controller` + `@ResponseBody`, 반환값을 JSON으로 응답 |
| `@RequestMapping` | 클래스 안 모든 메서드의 공통 URL 지정 |
| `@PathVariable` | URL 경로의 `{id}` 값을 매개변수로 받음 |
| `@RequestBody` | 요청 본문의 JSON을 객체로 변환 |
| `@RequiredArgsConstructor` | `final` 필드를 받는 생성자를 만들어 생성자 주입 |

필드에 `@Autowired`를 붙이는 대신 `final` + `@RequiredArgsConstructor`로 **생성자 주입**을 했다. 의존 객체가 바뀌지 않게 보장되고, 테스트할 때 직접 객체를 넣어 줄 수 있어 권장되는 방식이다.

`ResponseEntity`를 사용하면 응답 데이터뿐 아니라 HTTP 상태 코드도 직접 정할 수 있다. 등록은 `201 Created`, 삭제는 응답 본문이 없으니 `204 No Content`, 없는 번호를 요청하면 `404 Not Found`를 반환하도록 했다.

#### 6. JUnit으로 Service 테스트

```java
class TodoServiceTests {

    private final TodoService todoService = new TodoService();

    @Test
    void 저장_후_조회() {
        Todo saved = todoService.save(Todo.builder().title("스프링 복습").build());

        assertEquals(1L, saved.getId());
        assertEquals("스프링 복습", todoService.findById(1L).get().getTitle());
    }
}
```

```
./gradlew test
tests="2" skipped="0" failures="0" errors="0"
```

Service는 스프링 없이 `new TodoService()`로 바로 만들 수 있어서 `@SpringBootTest` 없이 빠르게 테스트할 수 있었다. JUnit은 테스트 메서드마다 테스트 클래스 객체를 새로 만들기 때문에, 각 테스트가 빈 저장소에서 시작해 번호가 1부터 매겨진다.

#### 핵심 정리

- `@Controller`는 화면(HTML)을, `@RestController`는 JSON 데이터를 반환한다.
- REST API는 URL로 자원을 표현하고 GET·POST·PUT·DELETE로 동작을 구분한다.
- Controller는 요청·응답, Service는 실제 처리로 역할을 나누면 코드를 이해하고 테스트하기 쉬워진다.
- `@PathVariable`은 URL 경로 값, `@RequestBody`는 요청 JSON을 받는다.
- `ResponseEntity`로 200, 201, 204, 404 같은 상태 코드를 상황에 맞게 반환할 수 있다.
- `final` + `@RequiredArgsConstructor`로 생성자 주입을 하면 의존 객체가 바뀌지 않고 테스트하기 쉽다.
- `Optional`은 값이 없을 수 있음을 드러내 `null` 처리 실수를 줄인다.
- `AtomicLong`은 여러 요청이 동시에 들어와도 번호가 겹치지 않게 증가시킨다.

---

MVC 실습 때는 `@Controller`와 Thymeleaf로 화면을 만들었는데, 이번에는 `@RestController`로 JSON만 주고받으니 화면 없이도 API를 완성할 수 있다는 점이 프로젝트에서 프론트엔드와 백엔드를 나눠 개발하는 방식과 연결되었다.
</br>처음에는 `ResponseEntity`를 왜 써야 하는지 잘 와닿지 않았는데, 없는 번호를 조회했을 때 그냥 `null`을 반환하면 200 OK로 빈 응답이 가서 클라이언트가 실패인지 알 수 없다는 점을 생각해 보니 상태 코드를 직접 정하는 이유를 이해할 수 있었다.
</br>또 `@Builder`만 붙였을 때 기본 생성자가 없어지는 문제나, 번호를 `AtomicLong`으로 매겨야 동시 요청에도 안전하다는 점처럼 작은 예제에서도 생각할 부분이 많았다.
</br>다음에는 메모리 저장소 대신 JPA와 DB를 연결하고, 제목이 비어 있으면 등록되지 않도록 `@Valid`로 입력값 검증까지 추가해 볼 예정이다.
