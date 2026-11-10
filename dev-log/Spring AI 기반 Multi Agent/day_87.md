# 87일차

## Spring AI: 외부 MCP 서버(Notion) 연결, STDIO 방식 MCP 클라이언트, 조회용 도구만 골라서 쓰는 Notion 문서 검색·요약 도우미

📌 학습일 : 2026.11.10

📌 학습 내용 : MCP(Model Context Protocol), 외부 MCP 서버, Notion MCP, MCP Client, STDIO 전송 방식, mcp-servers.json, servers-configuration, npx, mcp-remote, OAuth 인증, SyncMcpToolCallbackProvider, ToolCallback, ToolDefinition, getToolCallbacks(), toolCallbacks(), defaultToolCallbacks, 도구 필터링(조회용 도구만 허용), 시스템 프롬프트, request-timeout, temperature, Spring Boot 4.0.0, Spring AI 2.0.0

---

#### 1. 실습 프로젝트 구성

| 항목 | 내용 |
| --- | --- |
| 프로젝트 | demo-16-mcp |
| 목적 | 직접 만든 MCP 서버가 아니라 **외부 MCP 서버(Notion)** 에 연결해서, 내 Notion 문서를 검색하고 요약하는 도우미 만들기 |
| 버전 | Spring Boot 4.0.0, Spring AI 2.0.0 |
| 포트 | 8090 |

```
demo-16-mcp
└── src/main
    ├── java/com/example/demo
    │   ├── config/ChatConfig.java            # 도구 없는 기본 ChatClient
    │   ├── controller/NotionController.java   # /api/tools, /api/ask
    │   └── service/NotionChatService.java     # 조회용 도구만 골라서 질문
    └── resources
        ├── application.properties
        └── mcp-servers.json                   # 연결할 MCP 서버 목록 (Notion)
```

```
브라우저 ── /api/ask ──▶ Spring Boot (MCP 클라이언트)
                            │  STDIO (표준 입출력)
                            ▼
                    npx mcp-remote (내 PC에서 실행되는 중계 프로그램)
                            │  HTTPS
                            ▼
                    https://mcp.notion.com/mcp (Notion 공식 MCP 서버) ── 내 Notion 문서
```

#### 2. 어제와 오늘의 MCP 연결 방식 비교

| 구분 | 어제 (주문 조회 MCP) | 오늘 (Notion MCP) |
| --- | --- | --- |
| MCP 서버 | 내가 만든 스프링부트 서버 | Notion이 제공하는 외부 MCP 서버 |
| 전송 방식 | Streamable HTTP | STDIO |
| 연결 설정 | `streamable-http.connections.*.url` | `stdio.servers-configuration` + JSON 파일 |
| 서버 실행 | 서버를 먼저 직접 실행 | 클라이언트가 시작할 때 `npx` 명령으로 자동 실행 |
| 인증 | 없음 | Notion 계정 로그인 (OAuth) |

STDIO 방식은 MCP 클라이언트가 **MCP 서버 프로그램을 자식 프로세스로 실행하고, 그 프로그램과 표준 입력·출력으로 메시지를 주고받는** 방식이다. 그래서 서버 주소 대신 "어떤 명령으로 서버를 실행할지"를 설정한다.

#### 3. mcp-servers.json: 연결할 MCP 서버 정의

```json
{
  "mcpServers": {
    "notion": {
      "command": "cmd.exe",
      "args": [
        "/c",
        "npx",
        "-y",
        "mcp-remote",
        "https://mcp.notion.com/mcp"
      ]
    }
  }
}
```

| 항목 | 설명 |
| --- | --- |
| `mcpServers.notion` | 연결할 MCP 서버의 이름 |
| `command: cmd.exe`, `/c` | 윈도우에서는 `npx`가 배치 파일이라 `cmd.exe /c`를 거쳐 실행 |
| `npx -y` | 패키지를 설치 확인 없이 내려받아 바로 실행 |
| `mcp-remote` | 원격(HTTP) MCP 서버를 STDIO 방식으로 연결해 주는 중계 프로그램 |
| `https://mcp.notion.com/mcp` | Notion 공식 MCP 서버 주소 |

Notion MCP 서버는 인터넷에 있는 원격 서버인데, `mcp-remote`가 중간에서 STDIO ↔ HTTP를 바꿔 주기 때문에 스프링부트는 STDIO 방식으로 연결할 수 있다. 처음 실행하면 `mcp-remote`가 브라우저를 열어 Notion 로그인과 접근 허용을 요청하고, 허용한 페이지만 AI가 조회할 수 있다.

이 JSON 형식은 Claude Desktop 같은 다른 MCP 클라이언트의 설정 파일과 같은 형식이라, 다른 MCP 서버를 추가할 때도 `mcpServers` 아래에 한 항목만 더 쓰면 된다.

#### 4. application.properties

```properties
server.port=8090
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.2

spring.ai.mcp.client.enabled=true
spring.ai.mcp.client.type=SYNC
spring.ai.mcp.client.request-timeout=120s
spring.ai.mcp.client.stdio.servers-configuration=classpath:mcp-servers.json
logging.level.org.springframework.ai.mcp=INFO
```

| 설정 | 설명 |
| --- | --- |
| `spring-ai-starter-mcp-client` | MCP 클라이언트 의존성 (STDIO, Streamable HTTP 모두 지원) |
| `stdio.servers-configuration` | 실행할 MCP 서버 목록이 들어 있는 JSON 파일 위치 |
| `request-timeout=120s` | `npx`로 패키지를 내려받고 Notion 로그인까지 기다려야 해서 기본값보다 길게 설정 |
| `temperature=0.2` | 문서 내용을 지어내지 않고 일정하게 요약하도록 낮게 설정 |

API 키는 어제와 같이 `${OPENAI_API_KEY}` 환경변수로 받았다. Notion은 토큰을 파일에 쓰지 않고 `mcp-remote`의 로그인(OAuth)으로 인증하기 때문에 설정 파일에 비밀 값이 남지 않는다.

#### 5. 받아 온 도구 목록 확인 (/api/tools)

```java
public String getTools() {
	return Arrays.stream(provider.getToolCallbacks())
			.map(t -> t.getToolDefinition().name() + " : " + t.getToolDefinition().description())
			.collect(Collectors.joining("\n\n"));
}
```

| 코드 | 설명 |
| --- | --- |
| `SyncMcpToolCallbackProvider` | 연결된 MCP 서버(Notion)에서 받아 온 도구 묶음 |
| `getToolCallbacks()` | 도구 배열(`ToolCallback[]`)을 꺼냄 |
| `getToolDefinition().name()` | 도구 이름 |
| `getToolDefinition().description()` | LLM이 도구를 고를 때 읽는 설명 |

외부 MCP 서버는 내가 도구를 만든 것이 아니라서 어떤 도구가 있는지 모른다. 그래서 `/api/tools`로 **Notion MCP가 실제로 제공하는 도구 이름과 설명을 먼저 확인**하고 나서 질문 기능을 만들었다. Notion MCP에는 검색, 페이지 조회 같은 읽기 도구뿐 아니라 페이지 생성, 수정 같은 쓰기 도구도 함께 들어 있다.

#### 6. 조회용 도구만 골라서 질문하기 (/api/ask)

```java
@Bean
ChatClient chatClient(ChatModel model) {
	// No write tools are registered globally.
	return ChatClient.builder(model).build();
}
```

```java
public String ask(String question) {
	if (question == null || question.isBlank()) {
		throw new IllegalArgumentException("질문을 입력하세요.");
	}
	ToolCallback[] readTools = Arrays.stream(provider.getToolCallbacks())
			.filter(t -> isReadTool(t.getToolDefinition().name()))
			.toArray(ToolCallback[]::new);
	if (readTools.length == 0) {
		return "조회용 Notion MCP Tool을 찾지 못했습니다. /api/tools에서 실제 이름을 확인하세요.";
	}
	return chatClient.prompt()
			.system("""
				당신은 Notion 문서 검색 및 요약 도우미입니다.
				제공된 조회 도구로 실제 문서를 검색한 뒤 답변하세요.
				문서 생성, 수정, 삭제를 수행하지 마세요.
				확인되지 않은 내용은 만들어내지 마세요.
				한국어로 답변하세요.
				""")
			.user(question)
			.toolCallbacks(readTools)
			.call()
			.content();
}

private boolean isReadTool(String name) {
	String n = name.toLowerCase(Locale.ROOT);
	return n.contains("search") || n.contains("fetch") ||
	       n.contains("retrieve") || n.contains("list");
}
```

| 코드 | 설명 |
| --- | --- |
| `ChatClient.builder(model).build()` | 기본 ChatClient에는 도구를 하나도 등록하지 않음 |
| `isReadTool()` | 이름에 search, fetch, retrieve, list가 들어간 도구만 조회용으로 인정 |
| `.toolCallbacks(readTools)` | 이번 요청에만 조회용 도구를 넘김 |
| 시스템 프롬프트 | 문서 생성·수정·삭제 금지, 확인되지 않은 내용 금지 |

| 방식 | 차이 |
| --- | --- |
| `defaultToolCallbacks(provider)` (어제) | 받아 온 **모든** 도구를 ChatClient에 항상 등록 |
| `.toolCallbacks(readTools)` (오늘) | 요청할 때 **골라낸 도구만** 전달 |

Notion MCP에는 페이지를 만들고 고치는 도구도 있기 때문에, 모든 도구를 기본으로 등록하면 "정리해서 새 페이지로 만들어줘" 같은 질문에 LLM이 실제 문서를 바꿀 수 있다. 그래서 **LLM에게 쓰기 도구 자체를 주지 않는 방식**으로 막고, 프롬프트의 "수정하지 마세요"는 보조 규칙으로만 두었다. 프롬프트는 LLM이 어길 수 있지만, 넘기지 않은 도구는 아예 호출할 수 없다.

이름에 단어가 들어 있는지로 거르는 방식은 수업용 간단한 방법이라, 실무에서는 `/api/tools`로 확인한 실제 도구 이름을 목록(allowlist)으로 정해 두고 그 이름만 허용해야 한다. 도구 이름이 바뀌거나 이름에 `list`가 들어간 쓰기 도구가 생기면 잘못 통과할 수 있기 때문이다.

#### 7. 동작 흐름

| 순서 | 동작 |
| --- | --- |
| 1 | 앱이 시작되면 `mcp-servers.json`을 읽고 `npx mcp-remote`를 실행해 Notion MCP에 연결 |
| 2 | 처음 연결할 때 브라우저에서 Notion 로그인과 페이지 접근 허용 |
| 3 | `/api/tools`로 받아 온 도구 이름과 설명 확인 |
| 4 | `/api/ask?message=...`로 질문하면 조회용 도구만 골라 LLM에 전달 |
| 5 | LLM이 검색 도구로 문서를 찾고, 조회 도구로 내용을 읽은 뒤 요약해서 답변 |

#### 핵심 정리

- MCP는 직접 만든 서버뿐 아니라 Notion처럼 외부 서비스가 제공하는 MCP 서버에도 같은 방식으로 연결할 수 있다.
- STDIO 방식은 클라이언트가 MCP 서버 프로그램을 직접 실행하고 표준 입출력으로 통신하며, 실행 명령을 `mcp-servers.json`에 정의한다.
- `mcp-remote`는 원격(HTTP) MCP 서버를 STDIO 방식으로 연결해 주고, Notion 로그인(OAuth)으로 인증해서 토큰을 파일에 남기지 않는다.
- 외부 MCP 서버의 도구는 내가 만든 것이 아니므로, `getToolCallbacks()`로 실제 도구 이름과 설명을 먼저 확인해야 한다.
- `defaultToolCallbacks`는 모든 도구를 항상 등록하고, `.toolCallbacks()`는 요청마다 고른 도구만 넘긴다.
- 쓰기 도구가 섞인 MCP 서버는 조회용 도구만 넘겨서 LLM이 문서를 바꿀 수 없게 하고, 실무에서는 이름 목록(allowlist)으로 허용할 도구를 정한다.

---

```
java.io.IOException: Cannot run program "npx": CreateProcess error=2, 지정된 파일을 찾을 수 없습니다
```

처음에 `mcp-servers.json`의 `command`를 `npx`로 바로 적었더니 MCP 서버 프로그램을 실행하지 못했다. 윈도우에서 `npx`는 실행 파일이 아니라 `npx.cmd` 배치 파일이라서 자바가 바로 실행할 수 없다. `command`를 `cmd.exe`로 바꾸고 `args` 맨 앞에 `/c`를 넣어 명령 프롬프트를 거쳐 실행하도록 해서 해결했다.

```
java.lang.RuntimeException: Client failed to initialize by explicit API call
Caused by: java.util.concurrent.TimeoutException: Did not observe any item or terminal signal within 20000ms
```

처음 실행할 때는 `npx`가 `mcp-remote` 패키지를 내려받고, 브라우저에서 Notion 로그인과 접근 허용까지 마쳐야 연결이 끝나는데, 기본 대기 시간(20초) 안에 끝나지 않아 앱 시작이 실패했다. `spring.ai.mcp.client.request-timeout=120s`로 대기 시간을 늘려서 로그인할 시간을 확보했다. 한 번 로그인한 뒤에는 인증 정보가 저장되어 빠르게 연결된다.

```
Parameter 1 of constructor in com.example.demo.service.NotionChatService required a bean of type
'org.springframework.ai.mcp.SyncMcpToolCallbackProvider' that could not be found.
```

Notion 연결 없이 앱만 먼저 띄워 보려고 `spring.ai.mcp.client.enabled=false`로 바꿨더니, MCP 클라이언트가 만들어지지 않아 도구 묶음(`SyncMcpToolCallbackProvider`) 빈도 없어서 서비스를 만들지 못했다. 이 서비스는 MCP 도구가 꼭 필요하므로 `enabled=true`로 되돌리고 Notion 연결까지 마친 상태로 실행했다.

```
Web server failed to start. Port 8090 was already in use.
```

어제 실습한 주문 조회 MCP 서버도 8090 포트를 쓰는데, 그 서버가 켜진 상태에서 오늘 프로젝트를 실행해서 포트가 겹쳤다. 어제 서버를 종료하고 다시 실행해서 해결했다. 여러 실습 프로젝트를 함께 띄울 때는 포트를 미리 나눠 두는 것이 좋다.

```java
// ChatConfig.java : 기본 ChatClient 에는 도구를 등록하지 않음
return ChatClient.builder(model).build();

// NotionChatService.java : 요청할 때 조회용 도구만 골라서 전달
.toolCallbacks(readTools)
```

</br>외부 MCP 서버에는 조회 도구와 쓰기 도구가 함께 들어 있어서, 받아 온 도구를 모두 등록하면 LLM이 질문에 따라 실제 문서를 만들거나 고칠 수 있다는 것을 알게 되었다.
</br>프롬프트로 "수정하지 마세요"라고 쓰는 것보다, 처음부터 조회용 도구만 넘겨서 쓰기 도구를 호출할 방법 자체를 없애는 것이 더 확실한 방법이라는 것을 배웠다.
</br>또 STDIO 방식은 서버 주소 대신 실행 명령을 설정하고, `mcp-remote`를 쓰면 원격 MCP 서버도 같은 방식으로 연결할 수 있다는 것을 배웠다.

</br>어제는 내가 만든 MCP 서버에 연결했는데, 오늘은 Notion이 만든 MCP 서버에 설정 파일 한 줄로 연결되는 것을 보면서 MCP가 왜 표준 프로토콜인지 실감할 수 있었다.
</br>다만 외부 도구는 내가 만든 코드가 아니라서 어떤 일을 하는지 먼저 확인하고, AI에게 어디까지 맡길지 권한을 정하는 것이 중요하다고 느꼈다.

</br>오늘은 외부 MCP 서버인 Notion에 STDIO 방식으로 연결해서, 내 Notion 문서를 검색하고 요약하는 도우미를 만들어 보았다.
</br>도구 목록을 먼저 확인하고, 조회용 도구만 골라 넘기는 과정을 거치면서 외부 도구를 안전하게 쓰는 방법을 정리할 수 있었다.
</br>다음에는 이름 단어로 거르는 대신 실제 도구 이름으로 허용 목록을 만들고, 여러 MCP 서버를 함께 연결해서 문서 검색 결과를 다른 도구와 조합해 보고 싶다.
