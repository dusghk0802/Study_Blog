# 88일차

## Spring AI: 외부 MCP 서버(DeepWiki)로 GitHub 저장소 문서를 분석하는 Agent, Dockerfile로 이미지 만들기(FROM, COPY, RUN, ENTRYPOINT, nginx)

📌 학습일 : 2026.11.11

📌 학습 내용 : MCP(Model Context Protocol), 외부 MCP 서버, DeepWiki MCP, MCP Client, Streamable HTTP, streamable-http.connections, ToolCallbackProvider, defaultToolCallbacks, defaultSystem, ChatClient.Builder, Agent, 시스템 프롬프트, request-timeout, temperature, Docker, Dockerfile, 베이스 이미지, FROM, COPY, RUN, ENTRYPOINT, 이미지 레이어, 빌드 컨텍스트, docker build, docker run, docker exec, docker ps, 포트 매핑, nginx, 정적 웹 페이지

---

#### 1. 실습 프로젝트 구성

| 프로젝트 | 내용 |
| --- | --- |
| demo-17 | 외부 MCP 서버인 DeepWiki에 연결해서 GitHub 저장소(spring-projects/spring-ai)의 문서를 분석하는 Agent |
| docker_2 | `FROM`만 있는 가장 단순한 Dockerfile (node 이미지) |
| docker_3 | 파일 하나를 이미지에 복사하고, 컨테이너가 종료되지 않도록 실행 명령 지정 |
| docker_4 | 폴더 전체를 이미지에 복사 |
| docker_5 | nginx 이미지에 HTML, CSS를 넣어 정적 웹 페이지 띄우기 |
| docker_6 | `RUN`으로 이미지를 만들 때 git 설치 |

```
브라우저 ── /api/agent/ask ──▶ Spring Boot (MCP 클라이언트, 8080)
                                   │  Streamable HTTP
                                   ▼
                     https://mcp.deepwiki.com/mcp (DeepWiki MCP 서버)
                                   │
                                   ▼
                     GitHub 저장소를 분석해 만든 문서 (spring-projects/spring-ai)
```

#### 2. DeepWiki MCP란

DeepWiki는 GitHub 공개 저장소를 분석해서 구조, 주요 기능, 동작 방식을 위키 문서처럼 정리해 주는 서비스이고, 그 문서를 MCP 서버로도 제공한다. MCP 클라이언트로 연결하면 LLM이 저장소 문서를 조회하는 도구를 쓸 수 있어서, 모델이 학습하지 못한 최신 코드나 저장소 내용도 근거로 답할 수 있다.

| 구분 | 어제 (Notion MCP) | 오늘 (DeepWiki MCP) |
| --- | --- | --- |
| 연결 방식 | STDIO (`npx mcp-remote` 실행) | Streamable HTTP (주소로 바로 연결) |
| 설정 | `stdio.servers-configuration` + JSON 파일 | `streamable-http.connections.*` 두 줄 |
| 인증 | Notion 로그인 (OAuth) | 없음 (공개 저장소 문서) |
| 도구 | 조회 + 쓰기 도구가 섞여 있음 | 저장소 문서 조회용 도구 |
| 도구 등록 | 요청마다 조회용만 골라서 `.toolCallbacks()` | `defaultToolCallbacks`로 모두 등록 |

DeepWiki MCP는 문서를 읽는 도구만 제공하기 때문에, 어제처럼 도구를 골라내지 않고 받아 온 도구를 모두 기본 도구로 등록했다.

#### 3. application.properties

```properties
server.port=8080
spring.application.name=deepwiki-agent

# OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.2

# MCP Client
spring.ai.mcp.client.enabled=true
spring.ai.mcp.client.type=SYNC
spring.ai.mcp.client.request-timeout=60s
spring.ai.mcp.client.toolcallback.enabled=true

# 실제 외부 MCP Server
spring.ai.mcp.client.streamable-http.connections.deepwiki.url=https://mcp.deepwiki.com
spring.ai.mcp.client.streamable-http.connections.deepwiki.endpoint=/mcp
```

| 설정 | 설명 |
| --- | --- |
| `spring-ai-starter-mcp-client` | MCP 클라이언트 의존성 |
| `streamable-http.connections.deepwiki` | 연결 이름을 `deepwiki`로 정하고 주소와 엔드포인트를 지정 |
| `url` + `endpoint` | `https://mcp.deepwiki.com/mcp`로 연결 |
| `request-timeout=60s` | 저장소 문서를 조회하는 데 시간이 걸릴 수 있어 넉넉하게 설정 |
| `toolcallback.enabled=true` | MCP 서버의 도구를 Spring AI 도구(ToolCallback)로 변환해 빈으로 등록 |
| `temperature=0.2` | 문서 내용을 근거로 일정하게 답하도록 낮게 설정 |

Spring AI 1.1.0에서 MCP 클라이언트 연결 설정은 `stdio`, `sse`, `streamable-http` 세 가지 접두사만 있다. `http.connections`처럼 없는 접두사로 쓰면 에러 없이 무시되어 연결이 아예 만들어지지 않는다.

#### 4. AiConfig: DeepWiki 도구를 가진 Agent 만들기

```java
@Configuration
public class AiConfig {

	@Bean
	public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider mcpTools) {
		return builder
				.defaultSystem("""
					당신은 Spring AI 기술 문서 분석 Agent입니다.

					사용자가 GitHub 프로젝트에 대해 질문하면
					가능한 경우 DeepWiki MCP Tool을 사용하세요.

					기본 분석 대상 저장소는
					spring-projects/spring-ai 입니다.

					MCP Tool로 확인한 내용과
					일반적인 모델 지식을 구분하세요.

					답변은 한국어로 작성하세요.
					""")
				.defaultToolCallbacks(mcpTools)
				.build();
	}
}
```

| 코드 | 설명 |
| --- | --- |
| `ChatClient.Builder builder` | Spring AI가 자동으로 만들어 주는 빌더를 주입받음 (모델 설정이 이미 들어 있음) |
| `ToolCallbackProvider mcpTools` | MCP 클라이언트가 DeepWiki에서 받아 온 도구 묶음 |
| `defaultSystem(...)` | 모든 요청에 들어가는 시스템 프롬프트 |
| `defaultToolCallbacks(mcpTools)` | DeepWiki 도구를 모든 요청의 기본 도구로 등록 |

| 시스템 프롬프트 규칙 | 목적 |
| --- | --- |
| Spring AI 기술 문서 분석 Agent | 역할을 정함 |
| 가능한 경우 DeepWiki MCP Tool 사용 | 모델 기억보다 저장소 문서를 먼저 확인하게 함 |
| 기본 분석 대상은 spring-projects/spring-ai | 질문에 저장소 이름이 없어도 어느 저장소를 조회할지 알게 함 |
| MCP Tool로 확인한 내용과 모델 지식을 구분 | 근거가 있는 답과 추측을 사용자가 구별할 수 있게 함 |

DeepWiki 도구는 저장소 이름(`owner/repo`)을 인자로 받기 때문에, 시스템 프롬프트에 기본 저장소를 적어 두면 "ChatClient는 어떻게 동작해?"처럼 저장소 이름 없이 물어도 LLM이 `spring-projects/spring-ai`를 넣어 도구를 호출한다.

#### 5. AgentController

```java
@RestController
@RequestMapping("/api/agent")
public class AgentController {

	private final ChatClient chatClient;

	public AgentController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ask")
	public String ask(@RequestParam("question") String question) {
		return chatClient.prompt()
				.user(question)
				.call()
				.content();
	}
}
```

시스템 프롬프트와 도구는 `AiConfig`에서 기본값으로 넣어 두었기 때문에, 컨트롤러는 사용자 질문만 넘기면 된다. `localhost:8080/api/agent/ask?question=Spring AI의 ChatClient와 ChatModel 차이는?`처럼 물으면 LLM이 DeepWiki 도구로 저장소 문서를 조회한 뒤 답한다.

| 단계 | 동작 |
| --- | --- |
| 1 | 질문과 시스템 프롬프트, DeepWiki 도구 목록이 LLM에 전달됨 |
| 2 | LLM이 저장소 문서가 필요하다고 판단하면 DeepWiki 도구 호출을 요청 |
| 3 | Spring AI가 MCP 클라이언트로 DeepWiki 서버에 요청하고 결과를 LLM에 돌려줌 |
| 4 | LLM이 조회한 문서를 근거로 한국어 답변을 만듦 |

#### 6. Dockerfile 기본 명령어

| 명령어 | 실행 시점 | 설명 |
| --- | --- | --- |
| `FROM` | 빌드 | 바탕이 되는 베이스 이미지 (ubuntu, node, nginx 등) |
| `COPY` | 빌드 | 내 PC(빌드 컨텍스트)의 파일을 이미지 안으로 복사 |
| `RUN` | 빌드 | 이미지를 만들 때 명령을 실행하고, 그 결과를 이미지 레이어로 저장 |
| `ENTRYPOINT` | 컨테이너 실행 | 컨테이너가 시작될 때 실행할 명령 |

```dockerfile
# docker_2 : 베이스 이미지만 사용
FROM node
```

`FROM`만 있어도 이미지를 만들 수 있다. 이미 만들어진 node 이미지를 그대로 가져와 내 이름의 이미지로 만드는 것과 같다.

#### 7. COPY와 ENTRYPOINT: 파일 넣고 컨테이너 유지하기

```dockerfile
# docker_3 : 파일 하나 복사
FROM ubuntu
COPY app.txt /app.txt
ENTRYPOINT [ "/bin/bash", "-c", "sleep 500" ]
```

```dockerfile
# docker_4 : 폴더 전체 복사
FROM ubuntu
COPY ./ /
ENTRYPOINT ["/bin/bash", "-c", "sleep 500"]
```

| 코드 | 설명 |
| --- | --- |
| `COPY app.txt /app.txt` | 내 PC의 `app.txt`를 이미지의 `/app.txt`로 복사 |
| `COPY ./ /` | Dockerfile이 있는 폴더(빌드 컨텍스트) 전체를 이미지의 `/`에 복사 |
| `ENTRYPOINT [..., "sleep 500"]` | 컨테이너가 500초 동안 살아 있도록 실행 명령을 지정 |

ubuntu 이미지는 실행할 프로그램이 없어서 컨테이너를 실행하자마자 종료된다. `sleep 500`을 실행 명령으로 두면 그동안 컨테이너가 실행 상태로 남아서, `docker exec -it 컨테이너 bash`로 들어가 복사한 파일이 실제로 들어 있는지 확인할 수 있다.

`COPY ./ /`는 폴더 안의 모든 파일을 넣기 때문에 Dockerfile 자신과 필요 없는 파일까지 이미지에 들어간다. 빼고 싶은 파일은 `.gitignore`처럼 `.dockerignore` 파일에 적어서 빌드 컨텍스트에서 제외할 수 있다.

```
docker build -t my-ubuntu .
docker run -d --name myapp my-ubuntu
docker exec -it myapp bash
cat /app.txt          # hello world
```

#### 8. nginx로 정적 웹 페이지 띄우기

```dockerfile
# docker_5
FROM nginx
COPY ./ /usr/share/nginx/html
```

```html
<!DOCTYPE html>
<head>
 <meta charset="UTF-8">
 <link rel="stylesheet" href="style.css">
</head>
<body>
 <h1>My Web Page</h1>
</body>
</html>
```

```
docker build -t my-web .
docker run -d -p 8081:80 --name myweb my-web
```

| 코드 | 설명 |
| --- | --- |
| `FROM nginx` | 웹 서버 nginx가 설치된 이미지 |
| `/usr/share/nginx/html` | nginx가 웹 페이지 파일을 찾는 기본 폴더 |
| `-p 8081:80` | 내 PC의 8081 포트를 컨테이너의 80 포트(nginx)에 연결 |

nginx 이미지는 시작 명령이 이미 정해져 있어서 `ENTRYPOINT`를 쓰지 않아도 컨테이너가 계속 실행된다. `index.html`과 `style.css`를 nginx 기본 폴더에 복사하고 `http://localhost:8081`로 접속하면, CSS가 적용된 파란 글자의 `My Web Page`가 보인다.

#### 9. RUN으로 이미지에 프로그램 설치

```dockerfile
# docker_6
FROM ubuntu
RUN apt update && apt install -y git
ENTRYPOINT ["/bin/bash", "-c", "sleep 500"]
```

| 코드 | 설명 |
| --- | --- |
| `apt update` | 설치할 수 있는 패키지 목록을 최신으로 받음 |
| `apt install -y git` | git 설치, `-y`로 설치 확인 질문에 자동으로 yes |
| `&&` | 앞 명령이 성공하면 다음 명령 실행 |

`RUN`은 이미지를 **만들 때** 한 번 실행되고 그 결과가 이미지에 저장된다. 그래서 이 이미지로 만든 컨테이너는 실행할 때마다 git을 설치하지 않아도 바로 `git --version`을 쓸 수 있다. `ENTRYPOINT`는 반대로 컨테이너를 **실행할 때마다** 동작한다.

`apt update`와 `apt install`을 `RUN` 두 줄로 나누면, 두 번째 빌드부터 `apt update` 레이어가 캐시로 재사용되어 오래된 패키지 목록으로 설치를 시도할 수 있다. 그래서 `&&`로 한 줄에 묶는 것이 일반적이다.

#### 핵심 정리

- DeepWiki MCP는 GitHub 공개 저장소의 문서를 도구로 제공해서, LLM이 학습하지 못한 저장소 내용도 근거로 답하게 할 수 있다.
- Streamable HTTP 방식의 외부 MCP 서버는 `streamable-http.connections.이름.url`과 `endpoint` 두 줄로 연결한다.
- 없는 설정 접두사(`http.connections`)는 에러 없이 무시되므로, 연결이 안 되면 도구 목록이 비어 있지 않은지 먼저 확인해야 한다.
- `ChatClient.Builder`에 `defaultSystem`과 `defaultToolCallbacks`를 넣어 두면, 컨트롤러는 질문만 넘기는 Agent가 된다.
- 시스템 프롬프트에 기본 저장소와 "도구로 확인한 내용과 모델 지식을 구분"하는 규칙을 넣으면 근거 있는 답과 추측을 나눌 수 있다.
- Dockerfile의 `FROM`, `COPY`, `RUN`은 이미지를 만들 때, `ENTRYPOINT`는 컨테이너를 실행할 때 동작한다.
- 실행할 프로그램이 없는 이미지는 컨테이너가 바로 종료되므로, 확인용으로는 `sleep` 같은 실행 명령을 둔다.
- nginx 이미지는 `/usr/share/nginx/html`에 파일을 복사하고 `-p`로 포트를 연결하면 바로 웹 페이지를 띄울 수 있다.

---

```
MCP 도구 0개 등록, Agent가 DeepWiki를 호출하지 않고 일반 지식으로만 답변
```

앱은 에러 없이 실행되는데, 저장소 질문에 DeepWiki를 쓰지 않고 모델이 알고 있는 일반적인 내용으로만 답했다. 연결 설정을 `spring.ai.mcp.client.http.connections.deepwiki...`로 적었는데, Spring AI 1.1.0에는 `http`라는 연결 설정이 없어서 설정이 무시되고 MCP 연결이 하나도 만들어지지 않았다. `spring.ai.mcp.client.streamable-http.connections.deepwiki...`로 바꿔서 해결했다.

```
docker ps 목록에 컨테이너가 없음 (STATUS: Exited (0))
```

`ENTRYPOINT` 없이 ubuntu 이미지로 컨테이너를 실행했더니, 실행할 프로그램이 없어서 시작하자마자 정상 종료되었고 `docker exec`로 들어갈 수 없었다. `ENTRYPOINT ["/bin/bash", "-c", "sleep 500"]`을 넣어 컨테이너가 실행 상태로 남도록 해서 해결했다.

```
Do you want to continue? [Y/n] Abort.
The command '/bin/sh -c apt update && apt install git' returned a non-zero code: 1
```

`RUN apt install git`으로 빌드했더니 설치 확인 질문에 아무도 답할 수 없어서 설치가 취소되고 빌드가 실패했다. `apt install -y git`처럼 `-y` 옵션을 붙여 확인 질문에 자동으로 yes가 되도록 해서 해결했다.

```
사이트에 연결할 수 없음 (localhost에서 연결을 거부했습니다)
```

nginx 컨테이너를 `docker run -d my-web`으로만 실행해서 내 PC에서 접속할 포트가 연결되지 않았다. `docker run -d -p 8081:80 my-web`처럼 내 PC 포트와 컨테이너의 80 포트를 연결해서 해결했다.

```java
// AiConfig.java : DeepWiki 도구를 기본 도구로 등록한 Agent
return builder
		.defaultSystem("...기본 분석 대상 저장소는 spring-projects/spring-ai 입니다...")
		.defaultToolCallbacks(mcpTools)
		.build();
```

</br>DeepWiki MCP를 연결하니 LLM이 학습하지 못한 저장소 내용도 도구로 조회해서 답할 수 있었고, 외부 MCP 서버를 주소 두 줄만으로 연결할 수 있다는 것을 배웠다.
</br>또 설정 이름을 잘못 쓰면 에러 없이 무시되어 도구가 하나도 등록되지 않을 수 있어서, MCP 연결 후에는 도구 목록이 실제로 들어왔는지 확인해야 한다는 것을 알게 되었다.
</br>Dockerfile에서는 `FROM`, `COPY`, `RUN`은 이미지를 만들 때, `ENTRYPOINT`는 컨테이너를 실행할 때 동작한다는 차이를 직접 이미지를 만들면서 정리할 수 있었다.

</br>어제는 Notion, 오늘은 DeepWiki처럼 외부 MCP 서버를 하나씩 붙여 보니, 필요한 기능을 직접 만들지 않고 연결만으로 Agent의 능력을 늘릴 수 있다는 점이 인상적이었다.
</br>시스템 프롬프트에 "도구로 확인한 내용과 모델 지식을 구분하라"는 규칙을 넣은 것처럼, Agent가 답의 근거를 밝히게 해야 사용자가 답변을 믿고 쓸 수 있다고 느꼈다.
</br>Docker는 처음에는 명령어가 낯설었는데, 파일 하나 복사하기부터 nginx 웹 페이지와 프로그램 설치까지 단계별로 이미지를 만들어 보니 이미지와 컨테이너의 관계가 정리되었다.

</br>오늘은 외부 MCP 서버인 DeepWiki로 GitHub 저장소 문서를 분석하는 Agent를 만들고, Dockerfile로 여러 종류의 이미지를 직접 만들어 보았다.
</br>MCP로 외부 도구를 연결하는 방법과 Docker로 실행 환경을 이미지로 만드는 방법을 함께 익히면서, 만든 Agent를 다른 환경에서도 실행할 수 있는 기반을 다질 수 있었다.
</br>다음에는 오늘 만든 Agent를 Dockerfile로 이미지로 만들어 컨테이너로 실행해 보고, 여러 MCP 서버를 함께 연결해서 저장소 문서와 다른 도구를 조합해 보고 싶다.
