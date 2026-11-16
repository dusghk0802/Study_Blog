# 92일차

## Spring Boot 애플리케이션과 MySQL을 Docker Compose로 실행하기

📌 학습일 : 2026.11.16

📌 학습 내용 : Docker, Dockerfile, Docker Image, Container, Spring Boot, Gradle, MySQL, Docker Compose, Environment Variables, .env, .dockerignore

---

#### 1. 어제 학습일지와 비교해 달라진 점

- 어제는 공고·건물·공매 데이터가 언제 수집되고 어디에 저장되는지, ERD와 서비스 기능을 연결해 검토했다.
- 오늘은 설계한 애플리케이션을 실제 실행 환경에 올리는 과정에 집중했다. Spring Boot 실행 JAR을 만들고, Docker 이미지로 패키징한 다음, Docker Compose에서 애플리케이션과 MySQL을 함께 실행하도록 구성했다.
- 데이터베이스 연결도 로컬 주소가 아니라 Compose 서비스 이름을 사용한다. 애플리케이션의 JDBC URL은 `jdbc:mysql://mysql:3306/testdb`로 설정해 같은 Compose 네트워크의 `mysql` 서비스를 가리킨다.
- 실행 과정에서 나온 오류를 따라가며 Docker 데몬, 빌드 컨텍스트, 이미지와 컨테이너, YAML 구조, Gradle 태스크, 애플리케이션의 요청 매핑을 구분해 확인했다.

#### 2. Docker 이미지 빌드와 컨테이너 실행 흐름

- `docker build -t myweb .`에서 마지막 `.`은 Dockerfile과 복사 대상 파일을 찾는 빌드 컨텍스트다. 현재 폴더에 Dockerfile이 없거나 `COPY` 대상 파일이 컨텍스트 안에 없으면 이미지 빌드가 실패한다.
- Dockerfile의 `FROM`은 기반 이미지를 지정하고, `COPY`는 빌드 컨텍스트의 파일을 이미지 안으로 복사한다. Nginx 기본 웹 경로처럼 컨테이너 내부 경로를 정확히 써야 한다.
- `docker run`은 이미지에서 새 컨테이너를 만들고, `docker exec`는 이미 실행 중인 컨테이너 안에서 명령을 실행한다. 따라서 `docker ps`로 실행 중인 컨테이너의 ID나 이름을 확인하고 대괄호 없이 입력해야 한다.
- `docker run -p 80:80`은 호스트 80번 포트와 컨테이너 80번 포트를 연결한다. 이미지 이름이 로컬에 없으면 Docker가 레지스트리에서 찾으므로, 먼저 빌드한 이름과 실행할 때 쓰는 이름을 일치시켜야 한다.
- Docker가 Linux 엔진 파이프에 연결하지 못하면 빌드 명령보다 Docker Desktop의 실행 상태와 Linux 컨테이너 엔진 상태를 먼저 확인해야 한다.

#### 3. Gradle JAR과 Spring Boot·MySQL Compose 구성

- `clean`은 이전 빌드 결과를 지우는 Gradle 태스크이고 `bootJar`는 실행 가능한 Spring Boot JAR을 만든다. `claen`은 존재하지 않는 태스크이므로 `.\gradlew clean bootJar`로 실행한다.
- Dockerfile은 `eclipse-temurin:21-alpine`을 기반 이미지로 사용하고 `build/libs`에 생성된 JAR을 `app.jar`로 복사해 실행한다. JAR 파일은 프로젝트 루트가 아니라 `build/libs`에 생성된다.
- Compose 파일은 `services` 아래에 `mysql`과 `app`을 함께 선언한다. 서비스 이름은 내부 네트워크의 호스트 이름으로도 사용되므로, 앱은 `mysql:3306`으로 데이터베이스에 연결한다.
- MySQL에는 `testdb`를 만들고 데이터를 named volume인 `mysql-data`에 저장한다. 앱의 `depends_on`은 MySQL healthcheck가 통과한 뒤 앱이 시작되도록 설정한다.
- `/db-test` 컨트롤러에서는 `JdbcTemplate`으로 `SELECT VERSION()`과 `SELECT DATABASE()`를 실행해 MySQL 연결과 현재 DB를 확인한다.

| 구성 요소 | 역할 | 확인할 설정 |
| --- | --- | --- |
| Gradle `bootJar` | Spring Boot 실행 JAR 생성 | `build/libs`에 JAR이 있는지 확인 |
| Dockerfile | JAR을 Java 런타임 이미지에 복사하고 실행 | JAR 경로와 `ENTRYPOINT` |
| MySQL 서비스 | 애플리케이션 데이터 저장 | DB 이름, healthcheck, named volume |
| Spring Boot 앱 서비스 | API 실행 및 DB 연결 | `SPRING_DATASOURCE_URL`, 계정 정보 |
| Docker Compose 네트워크 | 서비스 간 통신 | JDBC 호스트를 `mysql`로 지정 |

#### 4. 설정 오류와 보안 정보 관리

- Compose 설정에서 `port`가 아니라 `ports`를 써야 하며, `app`도 최상위가 아니라 `services` 아래에 있어야 한다. named volume은 파일 마지막의 최상위 `volumes` 항목에 선언한다.
- `docker compose up` 전에 `docker compose config`로 YAML 구조와 변수 치환을 확인할 수 있다. Docker 명령은 현재 작업 폴더와 실제 파일 이름을 기준으로 동작하므로 `Dockerfile`이 있는 디렉터리에서 빌드해야 한다.
- 데이터베이스 비밀번호를 Compose 파일에 직접 기록하지 않고 `${MYSQL_ROOT_PASSWORD}`로 전달하도록 바꿨다. 예시 파일 `.env.example`에는 실제 비밀번호를 넣지 않고, 로컬 `.env`에 고유한 비밀번호를 설정한다.
- `.gitignore`에서 `.env`는 제외하고 `.env.example`은 커밋할 수 있도록 했다. `.dockerignore`에는 `.env`, Git 정보, Gradle 캐시와 불필요한 빌드 파일을 제외하고, Dockerfile이 필요로 하는 실행 JAR만 포함하도록 구성했다.
- 컨트롤러 코드에서 Java는 `sql:`, `key:`, `value:` 같은 이름 붙인 인자를 지원하지 않는다. 문자열과 인자를 일반 Java 문법으로 전달하고, `Map`을 사용할 때는 `java.util.Map`을 import해야 한다.

#### 5. 핵심 정리

- 이미지 빌드는 Dockerfile 위치뿐 아니라 빌드 컨텍스트와 `COPY` 경로까지 함께 맞아야 한다.
- 컨테이너는 이미지에서 만들어지고, `exec`는 실행 중인 컨테이너에 접속한다. 이미지 이름, 컨테이너 ID·이름, 포트 매핑은 각각 다른 설정이다.
- Gradle의 `bootJar` 결과 경로를 Dockerfile과 맞추고, Compose 서비스 이름을 JDBC 주소로 사용하면 애플리케이션과 MySQL이 컨테이너 네트워크에서 연결된다.
- Compose 구성은 들여쓰기와 계층이 의미를 가지므로 `services`, `ports`, `volumes`, `depends_on`의 위치를 확인해야 한다.
- 비밀번호는 소스와 업로드 파일에서 분리하고, `.env`는 Git에서 제외하며 예시 파일에는 비밀 값을 넣지 않는다.

---

오늘은 코드를 작성하는 것만으로 애플리케이션이 실행되는 것이 아니라, 빌드 산출물과 컨테이너 실행 환경까지 연결해야 한다는 점을 배웠다. 특히 빌드에 성공한 이미지 이름과 `docker run`에서 사용한 이름이 다르거나, Dockerfile이 없는 폴더에서 빌드하면 의도한 결과가 나오지 않는다. 오류 메시지를 볼 때 명령의 대상이 이미지인지 컨테이너인지, 현재 디렉터리가 빌드 컨텍스트인지부터 확인하는 습관이 필요하다고 느꼈다.
</br>Spring Boot와 MySQL을 Compose로 묶으면서 컨테이너 안에서 `localhost`는 각 컨테이너 자신을 뜻한다는 점도 중요했다. 앱은 MySQL 서비스 이름인 `mysql`로 연결하고, healthcheck로 DB 준비 상태를 확인한 뒤 시작한다. 이 구성은 서비스 이름, 포트, 환경 변수가 각각 어떤 역할을 하는지 이해해야 제대로 작동한다.

또한 편의를 위해 비밀번호를 Compose 파일에 직접 적으면 저장소에 올리는 순간 비밀 정보가 노출될 수 있다. 오늘은 비밀번호를 환경 변수로 분리하고 `.env`를 Git과 Docker 빌드 컨텍스트에서 제외했다. 앞으로는 애플리케이션 코드, 빌드 설정, 실행 설정, 비밀 정보를 각각 어디에 둘지 구분하고, 실행 전에 Compose 설정과 JAR 경로를 확인해야겠다.
