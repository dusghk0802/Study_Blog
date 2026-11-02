# 80일차

## Spring AI: ChatClient, System / User Message, Thymeleaf 연동, React 프론트 분리, Docker

📌 학습일 : 2026.11.02

📌 학습 내용 : Spring AI, spring-ai-bom, spring-ai-starter-model-openai, API Key 환경변수, ChatModel, ChatClient, prompt().call().content(), ApplicationContext.getBean(), SystemMessage, UserMessage, 텍스트 블록("""), MessageFormat, Lombok(@Data), 폼 데이터 바인딩, Model, Thymeleaf(th:action, th:text), @RestController, @RequestBody, JSON 응답, React 컴포넌트 분리, fetch, Vite proxy, npm run dev / build, WSL 2, Docker Desktop, docker run, 컨테이너

---

#### 1. 실습 프로젝트 구성


| 프로젝트 | 내용 | 화면 |
| --- | --- | --- |
| demo | 앱 실행 시 AI에게 질문하고 콘솔에 결과 출력 | 없음 (콘솔) |
| demo-1 | 제품 정보를 입력하면 AI가 마케팅 문구 생성 | Thymeleaf (8090) |
| demo2 | demo-1의 화면을 React로 분리 | React + Vite (5173 → 8090) |

세 프로젝트 모두 Spring Boot 3.5.7, Java 21, Gradle로 만들었고 Spring AI 1.0.3을 사용했다.

#### 2. Spring AI 프로젝트 설정

```groovy
ext {
	set('springAiVersion', "1.0.3")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.ai:spring-ai-starter-model-openai'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	...
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
	}
}
```

```properties
spring.application.name=demo

##발급받은 키를 사용
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

| 설정 | 역할 |
| --- | --- |
| `spring-ai-bom` | Spring AI 관련 라이브러리 버전을 한 번에 맞춰 줌 (각 의존성에 버전을 안 써도 됨) |
| `spring-ai-starter-model-openai` | OpenAI 연결용 `ChatModel`, `ChatClient.Builder`를 자동으로 Bean 등록 |
| `spring.ai.openai.api-key` | OpenAI API Key |
| `${OPENAI_API_KEY}` | 키를 파일에 직접 쓰지 않고 **환경변수**에서 읽어 옴 |

API Key를 `application.properties`에 그대로 적으면 깃허브에 올릴 때 키가 같이 공개된다. 그래서 키는 Windows 시스템 환경변수 `OPENAI_API_KEY`에 등록하고, 설정 파일에는 `${OPENAI_API_KEY}`처럼 변수 이름만 적었다.

#### 3. demo: ChatModel → ChatClient로 AI 호출

```java
@Service
public class ChatService {

	@Autowired
	private ChatModel chatmodel;

	public void run() {
		ChatClient chatClient = ChatClient.builder(chatmodel).build();

		String response = chatClient
				.prompt("유명한 명언 2개만 알려줘")
				.call()
				.content();
		System.out.println("결과 : " + response);
	}
}
```

```java
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		ChatService chatService = context.getBean(ChatService.class);
		chatService.run();
	}
}
```

| 코드 | 설명 |
| --- | --- |
| `ChatModel` | 실제로 OpenAI와 통신하는 모델 객체 (starter가 자동 등록) |
| `ChatClient.builder(chatmodel).build()` | ChatModel을 감싸서 쉽게 질문할 수 있게 만든 클라이언트 |
| `.prompt("...")` | AI에게 보낼 질문 |
| `.call()` | 요청을 보내고 응답을 기다림 (동기 방식) |
| `.content()` | 응답에서 텍스트만 꺼냄 |
| `context.getBean(ChatService.class)` | 스프링이 만든 Bean을 직접 꺼내서 사용 |

`main`은 static 메서드라 `@Autowired`를 쓸 수 없다. 그래서 `SpringApplication.run()`이 돌려주는 `ApplicationContext`에서 `getBean()`으로 `ChatService`를 꺼내 `run()`을 호출했다. 앱이 켜지자마자 AI에게 질문하고 콘솔에 결과가 출력된다.

#### 4. demo-1: 마케팅 문구 생성기 구조

```
브라우저 GET /marketing  →  marketing_request.html (입력 폼)
        │ 제품이름, 가격, 구매링크, 특징 입력 후 [생성]
        ▼
브라우저 POST /marketing →  MarketingController
        │ SystemMessage + UserMessage → ChatClient → OpenAI
        ▼
marketing_response.html (AI가 만든 마케팅 문구 출력)
```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class product {
	private String name;
	private int price;
	private String link;
	private String features;
}
```

```java
@Controller
public class MarketingController {

	private final ChatClient chatClient;

	//생성자 주입
	public MarketingController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/marketing")
	public String getMarketing() {
		return "marketing_request.html";
	}

	@PostMapping("/marketing")
	public String postMarketing(product product, Model model) { ... }
}
```

| 구분 | demo | demo-1 |
| --- | --- | --- |
| ChatClient 생성 | `ChatModel`을 주입받아 `ChatClient.builder(chatmodel)` | `ChatClient.Builder`를 생성자로 주입받아 `builder.build()` |
| 주입 방식 | 필드 주입 (`@Autowired`) | 생성자 주입 |

form의 `name` 속성(`name`, `price`, `link`, `features`)이 `product`의 필드 이름과 같아서, 컨트롤러 매개변수에 `product product`만 적어도 스프링이 입력값을 객체에 자동으로 넣어 준다. `@Data`는 Lombok이 getter, setter, `toString()`을 만들어 줘서 `System.out.println(product)`로 입력값을 바로 확인할 수 있었다.

#### 5. SystemMessage와 UserMessage

```java
//1. 시스템 메시지
var systemMessage = new SystemMessage(
	"""
	나는 마케팅 카피라이터 전문가야.

	입력된 제품 정보를 기반으로
	온라인 쇼핑몰/블로그/홍보 페이지에
	사용할 마케팅 문구를 작성해 줘.

	작성조건
	1. 첫 문장은 강렬하거나 공감 가는 표현 사용
	2. 제품 특징과 장점을 자연스럽게 설명
	3. 가격과 구매 링크를 포함
	4. 글자 수는 약 300~500자
	5. 캐주얼하지만 설득력 있는 톤
	6. 필요하면 이모지 활용
	"""
);

//2. 사용자 메시지
var userMessage = new UserMessage(
	MessageFormat.format("""
		입력정보

		-제품명 :{0}
		-가격 :{1}
		-구매링크  :{2}
		-제품특징 :{3}
		""",
		product.getName(),
		product.getPrice(),
		product.getLink(),
		product.getFeatures())
);

//3. ChatClient AI 호출
String result = chatClient.prompt()
		.messages(systemMessage, userMessage)
		.call()
		.content();
```

| 메시지 | 역할 | 예시 |
| --- | --- | --- |
| `SystemMessage` | AI의 역할, 말투, 규칙을 정함 | "나는 마케팅 카피라이터 전문가야", 작성 조건 6가지 |
| `UserMessage` | 실제로 처리할 사용자 입력 | 제품명, 가격, 구매링크, 특징 |

| 문법 | 설명 |
| --- | --- |
| `var` | 지역 변수 타입 추론 (Java 10~), 오른쪽 값을 보고 타입을 정함 |
| `"""  """` | 텍스트 블록 (Java 15~), 여러 줄 문자열을 `\n`과 `+` 없이 작성 |
| `MessageFormat.format()` | `{0}`, `{1}` 자리에 순서대로 값을 넣음 |

데모에서는 `.prompt("질문")`처럼 문자열 하나만 보냈지만, demo-1에서는 **역할(System)과 입력(User)을 나눠서** 보냈다. 같은 제품 정보를 넣어도 SystemMessage의 작성 조건에 따라 글자 수, 말투, 이모지 사용 여부가 달라진다.

#### 6. Thymeleaf로 결과 화면 전달

```java
//4. Thymeleaf 페이지 데이터 전달하기
model.addAttribute("marketingResult", result);
return "marketing_response.html";
```

```html
<!-- marketing_request.html -->
<form th:action="@{/marketing}" method="post">
	<input type="text" id="name" name="name" />
	<input type="text" id="price" name="price" />
	<input type="text" id="link" name="link" />
	<textarea id="features" name="features" cols="60" rows="8"></textarea>
	<button>생성</button>
</form>

<!-- marketing_response.html -->
<pre th:text="${marketingResult}" style="white-space: pre-wrap;"></pre>
<a href="/marketing" class="btn btn-primary">다시 생성하기</a>
```

```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

| 코드 | 설명 |
| --- | --- |
| `Model.addAttribute("이름", 값)` | 화면(View)으로 데이터 전달 |
| `return "파일명.html"` | `templates` 폴더의 파일을 화면으로 사용 |
| `th:action="@{/marketing}"` | form 전송 주소 |
| `th:text="${marketingResult}"` | 컨트롤러가 전달한 값을 출력 (HTML 태그는 문자로 처리) |
| `white-space: pre-wrap` | AI 응답의 줄바꿈을 그대로 보여 줌 |

`templates` 폴더의 html을 화면으로 쓰려면 Thymeleaf 의존성이 필요하다. 의존성이 있어야 `return "marketing_request.html"`을 `templates/marketing_request.html` 파일로 찾아서 화면을 만든다.

#### 7. demo2: 화면을 React로 분리

| 구분 | demo-1 (Thymeleaf) | demo2 (React) |
| --- | --- | --- |
| 화면을 만드는 곳 | 서버 (`templates/*.html`) | 브라우저 (React) |
| 컨트롤러 | `@Controller` → 화면 이름 반환 | `@RestController` → JSON 반환 |
| 입력값 받기 | form 데이터 → `product product` | JSON → `@RequestBody product product` |
| 결과 전달 | `model.addAttribute()` | `Map.of("marketingResult", result)` |
| 주소 | `GET`, `POST /marketing` | `POST /api/marketing` |

```java
@RestController //React 화면에 JSON으로 응답
@RequestMapping("/api")
public class MarketingController {

	//React 화면에서 보낸 제품 정보(JSON)를 받아서 처리
	@PostMapping("/marketing")
	public Map<String, String> postMarketing(@RequestBody product product) {
		...
		//결과를 React 화면으로 전달하기 ( { "marketingResult": "..." } )
		return Map.of("marketingResult", result);
	}
}
```

SystemMessage, UserMessage, ChatClient 호출 부분은 demo-1과 똑같이 두고, **입력을 받는 방법과 결과를 돌려주는 방법만** 바꿨다. 화면이 서버에서 만들어지지 않으니 Thymeleaf 의존성과 `templates` 폴더는 필요 없어졌다.

#### 8. React 컴포넌트 구조

```
frontend/src
├── main.tsx                         # React 시작점
├── App.tsx                          # 상태 관리 + API 호출
├── types.ts                         # Product 타입 (product.java와 같은 구조)
├── components
│   ├── MarketingRequest.tsx         # marketing_request.html → 입력 폼
│   └── MarketingResponse.tsx        # marketing_response.html → 결과 화면
├── App.css
└── index.css
```

```tsx
// App.tsx
const [product, setProduct] = useState<Product>(EMPTY)
const [marketingResult, setMarketingResult] = useState('')
const [loading, setLoading] = useState(false)

// POST /api/marketing → { marketingResult }
const handleSubmit = async () => {
  setLoading(true)
  try {
    const response = await fetch('/api/marketing', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...product, price: Number(product.price) || 0 }),
    })
    const data: { marketingResult: string } = await response.json()
    setMarketingResult(data.marketingResult)
  } finally {
    setLoading(false)
  }
}

return (
  <main className="container">
    {marketingResult ? (
      <MarketingResponse product={product} marketingResult={marketingResult} onReset={handleReset} />
    ) : (
      <MarketingRequest product={product} setProduct={setProduct} loading={loading} onSubmit={handleSubmit} />
    )}
  </main>
)
```

```tsx
// MarketingRequest.tsx : 입력값 4개를 하나의 함수로 처리
const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
  setProduct({ ...product, [e.target.name]: e.target.value })
}
```

| 코드 | 설명 |
| --- | --- |
| `useState` | 입력값, AI 결과, 로딩 여부를 상태로 관리 |
| `marketingResult ? A : B` | 결과가 있으면 결과 화면, 없으면 입력 폼 (페이지 이동 대신 조건부 렌더링) |
| `{ ...product, [e.target.name]: 값 }` | 스프레드 + 계산된 속성명으로 입력칸 4개를 함수 하나로 처리 |
| `JSON.stringify()` | 객체를 JSON 문자열로 바꿔 `@RequestBody`로 전송 |
| `Number(product.price)` | input 값은 문자열이라 `int price`에 맞게 숫자로 변환 |

Thymeleaf 버전에서는 [생성]을 누르면 페이지가 새로 바뀌었는데, React 버전은 같은 페이지에서 상태만 바뀌어서 화면이 전환된다. 생성 중에는 버튼에 로딩 표시가 나오고, 결과 화면에서는 복사 버튼과 [다시 생성하기] 버튼을 쓸 수 있게 했다.

#### 9. Vite 설정과 실행 방법

```ts
// vite.config.ts
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // 개발 중에는 /api 요청을 스프링부트(8090)로 전달
    proxy: {
      '/api': 'http://localhost:8090'
    }
  },
  build: {
    // 빌드 결과를 스프링부트 static 폴더에 넣어서 8090 하나로 실행
    outDir: '../src/main/resources/static',
    emptyOutDir: true
  }
})
```

| 상황 | 실행 | 주소 |
| --- | --- | --- |
| 개발 중 | VS Code에서 `frontend` 폴더 열고 `npm run dev` + STS에서 Spring Boot App 실행 | http://localhost:5173 |
| 완성 후 | `npm run build` → STS에서 Spring Boot App만 실행 | http://localhost:8090 |

| 설정 | 역할 |
| --- | --- |
| `proxy` | 5173에서 보낸 `/api` 요청을 8090으로 넘겨 줌 (CORS 설정 없이 개발 가능) |
| `outDir` | 빌드한 html, js, css를 스프링부트 `static` 폴더에 저장 |
| `emptyOutDir` | 빌드할 때마다 이전 빌드 파일을 지우고 새로 저장 |

개발할 때는 `npm run dev`로 저장하자마자 화면에 반영되는 것을 보면서 작업하고, 완성되면 `npm run build`로 스프링부트 안에 넣어서 서버 하나로 실행할 수 있다.

#### 10. Docker 설치와 실행

```
Windows 11
 └─ WSL 2 (Ubuntu)              ← 윈도우 안에서 리눅스 실행
     └─ Docker Desktop (엔진)    ← 리눅스 커널 위에서 컨테이너 실행
         ├─ hello-world 컨테이너
         └─ mysql 컨테이너
```

```bash
wsl --install -d Ubuntu          # WSL 2 + Ubuntu 설치 (관리자 PowerShell)
docker --version                 # Docker 설치 확인
docker run --rm hello-world      # 테스트 컨테이너 실행
```

```
Hello from Docker!
This message shows that your installation appears to be working correctly.
```

| 단계 | 내용 |
| --- | --- |
| 1 | Docker 클라이언트(`docker` 명령)가 Docker 데몬에게 요청 |
| 2 | 데몬이 Docker Hub에서 `hello-world` 이미지를 다운로드 |
| 3 | 받은 이미지로 컨테이너를 만들어 실행 |
| 4 | 컨테이너의 출력을 터미널로 전달 |

Docker Desktop의 **Settings → Resources → WSL integration**에서 Ubuntu를 켜면 Windows PowerShell뿐 아니라 Ubuntu 터미널에서도 `docker` 명령을 쓸 수 있다.

```bash
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=비밀번호 mysql:8.0
docker exec -it mysql mysql -uroot -p1234
docker ps -a
```

| 옵션 | 설명 |
| --- | --- |
| `-d` | 백그라운드 실행 |
| `--name mysql` | 컨테이너 이름 지정 |
| `-p 3306:3306` | 내 PC 포트 : 컨테이너 포트 연결 |
| `-e MYSQL_ROOT_PASSWORD=비밀번호` | 환경변수로 root 비밀번호 설정 |
| `mysql:8.0` | 이미지 이름:태그(버전) |
| `--rm` | 컨테이너가 종료되면 자동 삭제 |

MySQL을 PC에 직접 설치하지 않고 명령 한 줄로 컨테이너를 띄워서 `localhost:3306`으로 접속할 수 있었다. 필요 없으면 `docker rm -f mysql`로 지우면 되니 PC 환경이 지저분해지지 않는다.

#### 핵심 정리

- Spring AI는 `spring-ai-bom`으로 버전을 맞추고 `spring-ai-starter-model-openai`를 추가하면 `ChatModel`, `ChatClient.Builder`를 바로 주입받아 쓸 수 있다.
- API Key는 설정 파일에 직접 쓰지 않고 `${OPENAI_API_KEY}`처럼 환경변수로 읽어 온다.
- `ChatClient`는 `prompt() → call() → content()` 순서로 질문하고 응답 텍스트를 받는다.
- `SystemMessage`로 AI의 역할과 규칙을 정하고, `UserMessage`로 실제 입력을 보낸다.
- 텍스트 블록(`"""`)과 `MessageFormat.format()`으로 여러 줄 프롬프트에 입력값을 끼워 넣을 수 있다.
- `templates` 폴더의 html을 화면으로 쓰려면 Thymeleaf 의존성이 필요하다.
- 화면을 React로 분리하면 컨트롤러는 `@RestController` + `@RequestBody`로 JSON을 주고받는다.
- AI 호출 로직은 그대로 두고 입력과 출력 방식만 바꾸면 Thymeleaf에서 React로 옮길 수 있다.
- Vite의 `proxy`로 개발 중 API를 연결하고, `outDir`로 빌드 결과를 스프링부트 `static`에 넣는다.
- Docker는 WSL 2 위에서 동작하며, `docker run`으로 MySQL 같은 프로그램을 설치 없이 컨테이너로 실행할 수 있다.

---

```java
@GetMapping("/marketing")
public String getMarketing() {
	return "marketing_request.html";
}
```

```
No static resource marketing_request.html.
404 Not Found
```

demo-1을 처음 실행했을 때 컨트롤러도 있고 `templates` 폴더에 html도 있는데 `/marketing`에 들어가면 404가 났다.
</br>에러 메시지를 보니 스프링이 `templates`가 아니라 `static` 폴더에서 파일을 찾고 있었는데, build.gradle에 **Thymeleaf 의존성이 없어서** 화면 이름을 템플릿 파일로 바꿔 줄 엔진이 없었기 때문이었다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

의존성을 추가하고 Gradle을 새로고침하니 입력 폼이 정상적으로 나왔고, 컨트롤러에서 화면 이름을 반환하는 것만으로는 화면이 만들어지지 않고 그걸 처리해 주는 템플릿 엔진이 있어야 한다는 것을 알게 되었다.

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

```
HTTP 401 - Incorrect API key provided: ${OPENAI_API_KEY}.
```

화면은 떴지만 [생성]을 누르니 401 에러가 났는데, 메시지를 자세히 보니 실제 키가 아니라 `${OPENAI_API_KEY}`라는 글자가 그대로 OpenAI로 전송되고 있었다.
</br>시스템 환경변수에 키를 등록해 두었지만 STS가 그 전부터 켜져 있어서, STS와 STS로 실행한 앱이 새 환경변수를 알지 못했던 것이 원인이었다.
</br>실행 중인 프로그램은 켜질 때의 환경변수만 기억하기 때문에, 환경변수를 바꾼 뒤에는 STS를 완전히 껐다가 다시 켜야 한다는 것을 배웠다.
</br>또 설정 파일의 `${변수명}`과 시스템에 등록한 환경변수 이름이 한 글자라도 다르면 같은 401 에러가 나기 때문에, 이름을 정확히 맞추는 것도 중요하다는 것을 느꼈다.

```java
@RestController
@RequestMapping("/api")
public class MarketingController {

	@PostMapping("/marketing")
	public Map<String, String> postMarketing(@RequestBody product product) {
		...
		return Map.of("marketingResult", result);
	}
}
```

demo-1을 React로 바꿀 때 가장 많이 고민한 부분은 AI를 호출하는 코드를 얼마나 바꿔야 하는지였는데, 실제로는 `@Controller`를 `@RestController`로, form 데이터를 `@RequestBody`로, `model.addAttribute()`를 `Map` 반환으로 바꾸는 것만으로 충분했다.
</br>프롬프트와 ChatClient 호출 코드는 한 줄도 바꾸지 않아서, 화면과 비즈니스 로직을 나눠 두면 화면 기술이 바뀌어도 서버 로직은 그대로 쓸 수 있다는 점이 와닿았다.

```
npm error code ENOENT
npm error path C:\Users\kosa\Documents\edu\AILabs\demo2\package.json
npm error enoent Could not read package.json
```

```bash
cd frontend
npm install
npm run dev
```

VS Code에서 프론트를 따로 실행하려고 `npm install`을 입력했는데 package.json을 찾을 수 없다는 에러가 났다.
</br>React 프로젝트의 package.json은 `demo2\frontend` 폴더 안에 있는데, 나는 스프링부트 프로젝트 최상위인 `demo2` 폴더에서 명령을 실행하고 있었다.
</br>`cd frontend`로 이동한 뒤 실행하니 정상적으로 동작했고, 하나의 프로젝트 안에 백엔드(Gradle)와 프론트(npm)가 같이 있을 때는 지금 터미널이 어느 폴더에 있는지 먼저 확인해야 한다는 것을 배웠다.

```ts
proxy: {
  '/api': 'http://localhost:8090'
}
```

프론트(5173)와 백엔드(8090)를 따로 실행해 보니 Vite의 proxy 설정 덕분에 CORS 설정 없이도 `/api` 요청이 스프링부트로 넘어가는 것을 확인할 수 있었고, 실무에서 프론트와 백엔드를 나눠 개발하는 방식이 어떤 흐름인지 감을 잡을 수 있었다.

</br>Docker는 처음 써 봤는데, MySQL을 PC에 직접 설치하지 않고 명령어 한 줄로 실행하고 필요 없으면 바로 지울 수 있다는 점이 편리했다.
</br>오늘 Spring AI로 만든 프로젝트도 나중에 Docker 이미지로 만들어 MySQL 컨테이너와 함께 실행해 보고 싶다.
</br>같은 기능을 콘솔 → Thymeleaf → React 순서로 바꿔 보면서 하나의 AI 기능이 화면 방식에 따라 어떻게 구조가 달라지는지 비교할 수 있었던 하루였다.
