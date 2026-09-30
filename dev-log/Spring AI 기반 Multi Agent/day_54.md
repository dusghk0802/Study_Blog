# 54일차

## 미니프로젝트 개발 환경 세팅 및 TMDB 영화 데이터 가져오기 구현

📌 학습일 : 2026.09.30

📌 학습 내용 : Development Environment, Environment Variables, TMDB API, RestClient, Record DTO, Admin API, Spring Security, ApplicationRunner, Cache-Aside

---

#### 1. 개발 환경 세팅

- 팀 저장소를 ZIP이 아닌 **GitHub Desktop Clone**으로 받아야 브랜치·커밋·푸시 가능 (`.git` 폴더 유무)
- MySQL Workbench에서 `smart_ticketing` 데이터베이스 생성, 테이블은 `ddl-auto=update`로 자동 생성
- `application.properties`의 **`${이름:기본값}`** 규칙 이해
  - 기본값이 있으면 생략 가능 (`DB_URL`, `DB_USERNAME`)
  - 기본값이 없으면 반드시 설정 (`DB_PASSWORD`, `JWT_SECRET`, `TMDB_ACCESS_TOKEN`, `ADMIN_KEY`)
- 비밀값은 프로젝트 최상위 **`.env`** 에 두고 IntelliJ 실행 구성에서 불러오며, `.gitignore`로 커밋 제외
- 백엔드는 **IntelliJ(`SmartTicketingApplication`)**, 프론트는 **VS Code 터미널(`frontend` 폴더에서 `npm run dev`)** 로 실행

---

#### 2. ERD 및 요구사항 정의서 추가 수정

| 구분 | 변경 전 | 변경 후 |
| --- | --- | --- |
| 선호 좌석 | 위치만 저장 | `priority` 추가, `(user_id, priority)` Unique |
| 생년월일 | `NOT NULL` | NULL 허용 (소셜 회원은 로그인 후 입력) |
| 영화 | 외부 API ID 없음 | `tmdb_movie_id` 추가 (Unique, 동기화 기준) |
| 극장 좌표 | 저장 여부 미정 | 저장하지 않고 카카오 검색 결과의 좌표·거리 사용 |

- 선호 극장에 선택한 영화가 없으면 **GPS 현재 위치 기준 주변 극장 추천** 요구사항 추가
- 관리자 화면 대신 **관리자 API**로 운영하도록 요구사항·비기능 요구사항 보완
- 요구사항 정의서 최종 : 기능 **79개**, 비기능 **20개** (테스트 환경 분리, TMDB 약관 준수, 범위 단순화 등)

---

#### 3. TMDB 영화 가져오기 구현

팀원이 만든 계층형 패키지 구조(`controller`, `service`, `repository`, `dto`, `entity`, `config`)에 맞춰 구현하였다.

| 파일 | 역할 |
| --- | --- |
| `Movie` | `tmdbMovieId` 컬럼 추가 |
| `MovieRepository` | `existsByTmdbMovieId()`로 저장 여부 확인 |
| `TmdbConfig` | 기본 주소와 `Bearer` 토큰을 붙인 `RestClient` Bean 등록 |
| `TmdbMovieDetailResponse` | TMDB JSON 응답을 받는 **Record DTO** |
| `MovieImportResult` | 저장·건너뜀·실패 건수를 담는 결과 DTO |
| `MovieImportService` | DB에 없는 영화만 TMDB에서 가져와 `Movie`로 변환 후 저장 |
| `AdminMovieController` | `POST /api/admin/movies/import`, `X-Admin-Key` 헤더 검증 |

- 요청 : `/movie/{id}?language=ko-KR&append_to_response=release_dates`
  - `language=ko-KR`로 한국어 제목·줄거리 수신 (브라우저 번역과 무관)
  - 관람등급은 나라별로 있어 `release_dates`에서 **`KR` 등급만** 골라 저장
- `@JsonProperty`로 `release_date` ↔ `releaseDate` 이름 연결, `@JsonIgnoreProperties(ignoreUnknown = true)`로 쓰지 않는 값 무시
- 한 편이 실패해도 나머지는 계속 진행하고 실패한 번호만 따로 기록
- 영화 20편 목록은 코드가 아닌 `tmdb.movie-ids` 설정값으로 관리

---

#### 4. 필요할 때만 호출하는 자동 가져오기

- **Cache-Aside(지연 로딩)** 방식 적용 : DB를 먼저 확인하고, 없을 때만 TMDB 호출 후 저장
- `ApplicationRunner`를 구현한 `MovieDataInitializer`로 **서버 시작 시 한 번** 실행
  - 처음 실행 : DB가 비어 있어 20편 저장
  - 재실행 : 모두 건너뛰어 **TMDB 호출 0회**
- `tmdb.auto-import` 설정으로 테스트·부하 테스트 시 자동 실행 끄기
- TMDB에 한국 등급이 없는 영화는 SQL 수동 실행 대신 `tmdb.rating-overrides=1108427:ALL`처럼 **설정값으로 보완**
  - 엔티티 기본값은 모든 영화에 같은 등급이 들어가므로 부적합

---

#### 5. 테스트 결과

- Postman으로 관리자 API 호출 → **`200 OK`, 저장 20건, 실패 0건**
- MySQL에서 20편의 한국어 제목, 개봉일, 러닝타임, 포스터 주소, 관람등급 저장 확인
- 관람등급이 비어 있던 영화(모아나)를 보완하여 **등급 없는 영화 0건** 확인
- 서버 재시작 시 로그 `영화 가져오기 완료 - 저장 0건, 건너뜀 20건, 실패 0건`으로 **재호출 없음** 확인
- 작업 브랜치 `feat/movie-collect`에서 커밋 후 **main 병합 완료**

---

#### 6. Troubleshooting

- `anyRequest().authenticated()` 때문에 관리자 API가 **401**로 막힘 → `/api/admin/**`를 `permitAll`에 추가하고 컨트롤러에서 관리자 키로 별도 검증

---

#### 7. 핵심 정리

- 환경변수의 `${이름:기본값}` 규칙과 `.env`로 **비밀값을 코드와 분리**하는 방법을 익힘
- `RestClient` + Record DTO로 **외부 API 응답을 엔티티로 변환·저장**하는 흐름을 구현함
- 관리자 화면 없이 **관리자 API와 서버 시작 시 자동 가져오기**로 영화 데이터를 운영하도록 구성함
- DB를 먼저 확인하는 **Cache-Aside 방식**으로 외부 API 호출을 최소화함

---

<p align="center">
  <img src="../../projects/mini-project-02/2026.09.30/movie-import.png" alt="day_54" width="700">
</p>

본격적으로 개발을 시작하려고 하니 코드를 작성하기 전에 저장소 연결, 데이터베이스, 환경변수, 실행 설정처럼 준비해야 할 것이 생각보다 많았다.
</br>특히 서버 설정 문제나 보안 필터 에러를 해결하면서, 에러 메시지의 원인이 적힌 부분을 정확히 읽는 습관이 중요하다는 것을 느꼈다.

영화 데이터를 가져오는 기능은 단순해 보였지만, DB에 없을 때만 외부 API를 호출하도록 바꾸면서 호출 횟수와 테스트 안정성까지 함께 고려하게 되었고, 다음에는 저장한 영화를 메인 화면에 보여주는 영화 차트 API를 만들 예정이다.