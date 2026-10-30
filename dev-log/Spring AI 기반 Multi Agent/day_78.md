# 78일차

## Spring Boot: 함수형 프로그래밍, 람다식, 함수형 인터페이스, 메서드 참조, Stream API / WebSocket 실시간 알림 게시판

📌 학습일 : 2026.10.30

📌 학습 내용 : 명령형 프로그래밍, 선언형 프로그래밍, 함수형 프로그래밍, 순수 함수, 일급 객체, 불변성, 람다식(Lambda Expression), 함수형 인터페이스, @FunctionalInterface, 익명 클래스, 메서드 참조(::), Stream API, 스트림 생성 / 중간 연산 / 최종 연산, filter, map, distinct, sorted, limit, peek, count, forEach, mapToInt, sum, WebSocket, STOMP, @EnableWebSocketMessageBroker, SimpleBroker, SimpMessagingTemplate, ConcurrentHashMap, AtomicLong, record, 폴링, 롱폴링, SSE

---

#### 1. 명령형 vs 선언형 프로그래밍

| 구분 | 명령형 (Imperative) | 선언형 (Declarative) |
| --- | --- | --- |
| 관심사 | **어떻게(How)** 할 것인가 | **무엇을(What)** 할 것인가 |
| 방식 | 컴퓨터가 수행할 명령을 순서대로 작성 | 원하는 결과만 작성, 처리 방법은 추상화 |
| 예 | C, 포트란, for + if | SQL, HTML, 함수형 프로그래밍 |
| 레스토랑 비유 | "창가 쪽 자리로 걸어가서 앉을게요" | "두 명 앉을 자리 부탁해요" |

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 명령형 : 반복과 조건을 직접 작성
int result = 0;
for (int n : list) {
    if (n % 2 == 0) result += n;
}

// 선언형 : Stream으로 무엇을 할지만 작성
int result2 = list.stream()
        .filter(n -> n % 2 == 0)
        .mapToInt(Integer::intValue)
        .sum();      // 30
```

선언형은 for, if 같은 처리 과정이 `filter`, `map` 안으로 추상화되어 가독성이 높아지고, 같은 함수를 재사용하므로 유지보수가 쉬워진다.

#### 2. 함수형 프로그래밍의 특징

함수형 프로그래밍은 선언형 프로그래밍을 따르는 대표적인 패러다임으로, 함수들의 집합으로 프로그램을 구성한다.

| 특징 | 의미 |
| --- | --- |
| 순수 함수 (Pure Function) | 같은 입력이면 항상 같은 결과, 실행해도 외부 상태를 바꾸는 부수 효과(side-effect)가 없음 |
| 일급 객체 (First-class) | 함수를 변수에 담고, 다른 함수의 인자로 전달하고, 결과로 반환할 수 있음 |
| 불변성 (Immutable) | 데이터를 직접 바꾸지 않고 새로운 인스턴스를 만들어 의도하지 않은 변경을 방지 |

JavaScript는 함수를 바로 변수에 담을 수 있지만, Java는 모든 함수가 클래스나 인터페이스 안의 메서드로만 존재하므로 **함수형 인터페이스를 빌려서** 람다식으로 표현한다.

#### 3. 람다식과 함수형 인터페이스

| 용어 | 의미 |
| --- | --- |
| 람다식 | 함수(메서드)를 하나의 식으로 표현한 것 `(매개변수) -> { 실행문 }` |
| 함수형 인터페이스 | **추상 메서드가 단 1개**만 있는 인터페이스, 람다식은 이 인터페이스로만 표현 가능 |
| `@FunctionalInterface` | 추상 메서드가 2개 이상이면 컴파일 에러를 내서 함수형 인터페이스임을 보장 |

**구현 클래스 → 익명 클래스 → 람다식으로 발전**

```java
@FunctionalInterface
interface MyFunction {
    void method();
}

// 1단계 : 구현 클래스  class B implements MyFunction { public void method(){...} }

// 2단계 : 익명 클래스 (클래스 이름 없이 인터페이스로 바로 객체 생성)
MyFunction myfunc = new MyFunction() {
    @Override
    public void method() {
        System.out.println("world");
    }
};

// 3단계 : 람다식 (구현부만 남김)
MyFunction my  = () -> System.out.println("world");
MyFunction my2 = () -> { System.out.println("world"); System.out.println("hello"); };
```

**입력과 리턴에 따른 람다식 형태**

| 인터페이스 | 입력 / 리턴 | 람다식 |
| --- | --- | --- |
| `void method1();` | X / X | `() -> System.out.println()` |
| `void method2(int a);` | O / X | `a -> System.out.println(a)` (매개변수 1개면 괄호와 타입 생략 가능) |
| `int method3();` | X / O | `() -> 4` (실행문이 return 하나면 `{}`와 return 생략) |
| `double method4(int a, double b);` | O / O | `(a, b) -> a + b` |

| 장점 | 단점 |
| --- | --- |
| 코드가 간결하고 가독성이 높음 | 이름 없는 함수라 재사용이 어려움 |
| Stream과 함께 병렬 처리에 강함 | 디버깅이 어렵고, 재귀 구현이 어려움 |

#### 4. 람다식 실습

```java
@FunctionalInterface
interface MyLamdaFunction {
    int max(int a, int b);
}

// 익명 객체를 만들자마자 호출
System.out.println(new MyLamdaFunction() {
    @Override
    public int max(int a, int b) { return a > b ? a : b; }
}.max(3, 5));                                                   // 5

MyLamdaFunction lamdaFunction = (int a, int b) -> a > b ? a : b;
System.out.println(lamdaFunction.max(10, 20));                  // 20
```

```java
@FunctionalInterface
interface MyFunc  { void method(int x); }

@FunctionalInterface
interface MyFunc2 { int method(int x, int y); }

MyFunc2 my2 = (x, y) -> x + y;
int result = my2.method(111, 222);                              // 333

MyFunc my3 = x -> System.out.println("param-x :" + x);
my3.method(1234);                                               // param-x :1234
```

람다식의 형태는 인터페이스의 추상 메서드가 정하므로, 리턴 타입이 `void`인 인터페이스에 값을 돌려주는 람다 `(x, y) -> x + y`를 넣으면 컴파일 에러가 난다.

#### 5. 메서드 참조 (::)

람다식이 **이미 있는 메서드 하나를 그대로 호출**하기만 할 때 `클래스(객체)::메서드이름`으로 더 짧게 쓴다.

```java
List<String> list = new ArrayList<>(List.of("java", "c", "react"));

list.stream().forEach((String str) -> { System.out.println(str); });   // 람다식
list.stream().forEach(str -> System.out.println(str));                 // 타입, {} 생략
list.stream().forEach(System.out::println);                            // 메서드 참조
```

| 종류 | 형태 | 예 |
| --- | --- | --- |
| 정적 메서드 참조 | `클래스::정적메서드` | `Integer::parseInt` |
| 인스턴스 메서드 참조 | `객체::메서드` | `System.out::println`, `b::bcd` |
| 특정 타입의 메서드 참조 | `클래스::인스턴스메서드` | `String::toUpperCase`, `Integer::intValue` |
| 생성자 참조 | `클래스::new` | `ArrayList::new` |

#### 6. Stream API

스트림은 컬렉션(List, Set, Map)과 배열 같은 다양한 데이터 소스를 **표준화된 방법**으로 다루기 위해 Java 1.8부터 제공되는 데이터의 연속적인 흐름(통로)이다.

```
컬렉션 / 배열  →  스트림 생성  →  중간 연산 (n번)  →  최종 연산 (1번)  →  결과
```

| 단계 | 내용 | 메서드 |
| --- | --- | --- |
| 1. 생성하기 | 배열, 컬렉션 등으로 Stream 객체 생성 | `Stream.of(배열)`, `list.stream()` |
| 2. 가공하기 (중간 연산) | 결과가 다시 Stream이라 계속 연결 가능 | `filter`, `map`, `distinct`, `sorted`, `limit`, `peek` |
| 3. 결과 만들기 (최종 연산) | 요소를 소모하며 결과 생성, 1번만 가능 | `count`, `forEach`, `sum`, `max`, `min`, `collect` |

**스트림의 특징**

- 원본 데이터를 변경하지 않고 **읽기만** 한다.
- Iterator처럼 **일회용**이라 최종 연산 후에는 다시 생성해서 사용해야 한다.

```java
String[] strarr = {"dd", "aaa", "CC", "cc", "h"};
Stream<String> stream = Stream.of(strarr);

stream = stream.filter(s -> s.length() <= 2);            // dd, CC, cc, h
stream = stream.distinct();                              // 중복 제거 (CC와 cc는 다른 값)
stream = stream.sorted(String.CASE_INSENSITIVE_ORDER);   // 대소문자 무시 정렬
stream = stream.limit(3);                                // 앞에서 3개
long count = stream.count();                             // Total count : 3

List<String> myList = Arrays.asList("a1", "a2", "b1", "b2", "c1");
count = myList.stream()
        .filter(s -> s.startsWith("c"))
        .map(String::toUpperCase)
        .sorted()
        .peek(System.out::println)                       // C1 (중간에 값 확인)
        .count();                                        // 1
```

#### 7. Stream 실습

| 문제 | 코드 | 결과 |
| --- | --- | --- |
| "a"가 들어간 이름 추출 | `list.stream().filter(name -> name.contains("a"))` | a, aa, aaa |
| 이름을 대문자로 바꿔 출력 | `names.stream().map(s -> s.toUpperCase())` | A, B, C, AA, AAA, BB |
| 정렬해서 출력 | `list3.stream().sorted()` | Go, Groovy, Java, Python, Scala, Swift |

```java
Stream<String> stream = list.stream().filter(name -> name.contains("a"));
stream.forEach(System.out::println);
```

#### 8. WebSocket 실시간 알림 게시판 (DB 없음)

게시판에 글을 작성하면 접속한 모든 사용자에게 실시간 알림이 발송되는 기능을 Spring Boot 4.1.0 + Gradle로 DB 없이 구현했다.

| 파일 | 역할 |
| --- | --- |
| `WebSocketConfig` | `@EnableWebSocketMessageBroker`, `/ws` 연결 주소, `/topic` 구독 주소, `/app` 발행 주소 설정 |
| `PostRepository` | DB 대신 `ConcurrentHashMap`에 저장, `AtomicLong`으로 글 번호 채번 (시퀀스 역할) |
| `PostController` | 목록·상세·작성 REST API, 글 저장 후 `SimpMessagingTemplate`으로 알림 발송 |
| `Post`, `PostRequest`, `Notification` | 게시글, 글쓰기 요청, 알림 메시지 (record) |
| `static/index.html` | STOMP 클라이언트로 구독, 글쓰기 폼, 글 목록, 알림 토스트 |

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");                       // 브라우저가 연결하는 주소
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");             // 서버 → 구독자
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트 → 서버
    }
}
```

```java
@PostMapping
public ResponseEntity<Post> write(@RequestBody PostRequest request) {
    ...
    Post post = postRepository.save(request.title().trim(), request.content().trim(), writer);

    Notification notification = new Notification(post.id(), post.title(), post.writer(),
            post.writer() + "님이 새 글을 작성했습니다.", LocalDateTime.now());
    messagingTemplate.convertAndSend("/topic/notifications", notification);   // 구독자 전체에게 전송

    return ResponseEntity.status(HttpStatus.CREATED).body(post);
}
```

```javascript
const stompClient = new StompJs.Client({
  brokerURL: `ws://${location.host}/ws`,
  reconnectDelay: 3000                        // 끊기면 3초 후 자동 재연결
});

stompClient.onConnect = () => {
  stompClient.subscribe("/topic/notifications", (message) => {
    const notification = JSON.parse(message.body);
    showToast(notification);                  // 알림 표시
    loadPosts(notification.postId);           // 목록 새로고침 + 새 글 강조
  });
};
stompClient.activate();
```

**동작 흐름**

1. 페이지가 열리면 `ws://서버주소/ws`로 WebSocket을 연결하고 `/topic/notifications`를 구독한다.
2. 글을 작성하면 `POST /api/posts`로 서버에 보낸다.
3. 서버는 메모리에 저장한 뒤 `convertAndSend()`로 구독자 전체에게 알림을 보낸다.
4. 접속한 모든 브라우저에 "○○님이 새 글을 작성했습니다" 알림이 뜨고 목록이 새로고침된다.

오늘 배운 람다식과 Stream도 게시판 코드에 그대로 쓰였다.

```java
return store.values().stream()
        .sorted(Comparator.comparing(Post::id).reversed())   // 메서드 참조로 최신 글 정렬
        .toList();

return postRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));
```

#### 9. 실시간 통신 방식 비교 (폴링, 롱폴링, SSE, WebSocket)

| 구분 | 폴링 | 롱폴링 | SSE | WebSocket |
| --- | --- | --- | --- | --- |
| 동작 | 정해진 간격마다 요청 | 새 데이터가 생길 때까지 응답 보류 | 연결 1개로 서버가 계속 전송 | 연결 1개로 서로 주고받음 |
| 통신 방향 | 클라이언트 → 서버 | 클라이언트 → 서버 | **서버 → 클라이언트** | **양방향** |
| 실시간성 | 낮음 | 높음 | 높음 | **매우 높음** |
| 서버 부하 | 높음 | 중간 | 낮음 | **가장 낮음** |
| 자동 재연결 | 해당 없음 | 직접 구현 | **브라우저 지원** | 직접 구현 |
| Spring | `@GetMapping` | `DeferredResult` | `SseEmitter` | `@EnableWebSocketMessageBroker` |
| 실무 사례 | 결제 상태 확인, 작업 진행률 | 메신저 봇, WebSocket 대체 경로 | AI 답변 스트리밍, 주문 상태 알림 | 채팅, 거래소 시세, 협업 편집 |

| 상황 | 추천 방식 |
| --- | --- |
| 양쪽이 자주 주고받는다 (채팅, 게임, 협업 편집) | WebSocket |
| 서버가 알리기만 한다 (알림, 주문 상태, AI 답변 스트리밍) | SSE |
| 몇 초에서 몇십 초 늦어도 된다 (결제 확인, 작업 상태) | 폴링 |
| SSE와 WebSocket을 쓸 수 없는 환경 | 롱폴링 |

WebSocket은 서버를 여러 대 두면 서버마다 연결이 따로 잡혀 `enableSimpleBroker`만으로는 다른 서버의 사용자에게 메시지가 가지 않으므로, 운영 환경에서는 Redis Pub/Sub이나 RabbitMQ 같은 외부 브로커를 사용한다.

#### 핵심 정리

- 명령형은 어떻게(How), 선언형은 무엇을(What)에 집중하며, 함수형 프로그래밍은 선언형의 대표적인 패러다임이다.
- 함수형 프로그래밍의 함수는 순수 함수, 일급 객체, 불변성의 특징을 갖는다.
- Java는 함수를 단독으로 쓸 수 없어 추상 메서드가 1개뿐인 함수형 인터페이스를 빌려 람다식으로 표현한다.
- 람다식은 구현 클래스 → 익명 클래스 → 람다식 순서로 줄여 쓴 것이며, 형태는 추상 메서드의 매개변수와 리턴 타입이 정한다.
- 이미 있는 메서드 하나만 호출하는 람다식은 `::` 메서드 참조로 더 짧게 쓸 수 있다.
- Stream은 생성 → 중간 연산(n번) → 최종 연산(1번) 순서로 처리하며, 원본을 바꾸지 않고 일회용이다.
- WebSocket(STOMP)은 `/ws`로 연결하고 `/topic`을 구독하며, 서버는 `SimpMessagingTemplate.convertAndSend()`로 구독자 전체에게 메시지를 보낸다.
- DB 없이 `ConcurrentHashMap`과 `AtomicLong`으로 동시 접속에도 안전한 메모리 저장소를 만들 수 있지만, 서버를 재시작하면 데이터가 사라진다.
- 서버가 알리기만 하면 SSE, 양방향으로 자주 주고받으면 WebSocket, 늦어도 되면 폴링을 선택한다.

---

```java
// WebSocketConfig.java : 연결 주소와 구독 주소 설정
registry.addEndpoint("/ws");
registry.enableSimpleBroker("/topic");

// PostController.java : 글 저장 후 구독자 전체에게 알림 발송
messagingTemplate.convertAndSend("/topic/notifications", notification);
```

```javascript
// index.html : 연결 후 알림 주소 구독
stompClient.subscribe("/topic/notifications", (message) => {
  const notification = JSON.parse(message.body);
  showToast(notification);
  loadPosts(notification.postId);
});
```

글을 작성하면 접속한 모든 사용자에게 실시간 알림이 가는 게시판을 WebSocket과 STOMP로 직접 만들어 보면서, 클라이언트가 요청해야만 응답하는 HTTP와 달리 서버가 먼저 데이터를 밀어 줄 수 있다는 것을 확인했다.
</br>브라우저 창 두 개를 띄워 한쪽에서 글을 쓰자 다른 쪽에도 바로 알림이 뜨는 것을 보며, 연결 주소(/ws), 구독 주소(/topic), convertAndSend로 이어지는 메시지 흐름을 이해할 수 있었다.

```java
// PostRepository.java : DB 대신 메모리에 저장
private final Map<Long, Post> store = new ConcurrentHashMap<>();
private final AtomicLong sequence = new AtomicLong(0);

public Post save(String title, String content, String writer) {
    Long id = sequence.incrementAndGet();   // DB 시퀀스 역할
    Post post = new Post(id, title, content, writer, LocalDateTime.now());
    store.put(id, post);
    return post;
}
```

DB 없이 ConcurrentHashMap과 AtomicLong으로 저장소를 만들면서 여러 사용자가 동시에 글을 써도 번호가 겹치지 않게 하려면 동시성까지 고려해야 한다는 것을 알게 되었고, 서버를 재시작하면 데이터가 사라지는 한계도 확인했다.

```java
// PostRepository.java : Stream + 메서드 참조로 최신 글 정렬
return store.values().stream()
        .sorted(Comparator.comparing(Post::id).reversed())
        .toList();

// PostController.java : 람다식으로 예외 생성
return postRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));
```

게시판 코드의 Comparator.comparing(Post::id)나 orElseThrow(() -> ...)처럼 오늘 배운 람다식과 메서드 참조, Stream이 실제 스프링 코드 곳곳에 쓰이는 것을 보며 함수형 문법을 익혀야 하는 이유를 체감했다.

```java
// SSE로 바꾼다면 (참고) : 서버 → 클라이언트 단방향 알림
@GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter subscribe() {
    SseEmitter emitter = new SseEmitter(60_000L);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    return emitter;
}
```

폴링, 롱폴링, SSE, WebSocket을 비교해 보니 지금처럼 새 글 알림만 보낸다면 SSE로도 충분하다는 것을 알게 되어, 기술을 고를 때는 통신 방향과 실시간성이 얼마나 필요한지를 먼저 따져야겠다고 느꼈다.
</br>다음에는 같은 게시판을 SSE 버전으로도 만들어 비교해 보고, 서버가 여러 대일 때 Redis Pub/Sub으로 메시지를 공유하는 방법도 공부해 보고 싶다.
