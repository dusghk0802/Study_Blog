# 51일차

## 신규 프로젝트 아이디어 기획 및 후보 주제 비교 분석

📌 학습일 : 2026.09.23

📌 학습 내용 : Project Ideation, Requirement Analysis, Public Data, ETL Pipeline, Search Optimization, OTT Subscription Management, Async Notification, Concurrency Control

---

#### 1. 신규 프로젝트 방향성 설정

- 단순 CRUD 중심의 프로젝트에서 벗어나 백엔드 기술적 도전 과제가 포함된 주제 탐색
- 실시간 데이터보다는 비동기·배치 처리 중심의 서비스 고려
- 대용량 데이터 처리, 검색 최적화, 트래픽 제어, 동시성 처리 등을 주요 기술 과제로 설정
- 사용자 편의성과 명확한 서비스 목적을 함께 고려

#### 2. 후보 1 : 고용24 기반 채용공고 비교·분석 플랫폼

- 고용24 OpenAPI를 활용한 채용공고 수집·가공
- 직무·지역·연봉·복지 등의 조건으로 공고 검색 및 비교
- 사용자 맞춤 공고 스크랩 및 추천
- Spring Batch / Scheduler를 활용한 공공데이터 ETL 파이프라인 구성
- Elasticsearch 또는 Composite Index를 활용한 검색 성능 최적화

**핵심 기술:** `Spring Batch` · `ETL` · `Elasticsearch` · `Composite Index`

#### 3. 후보 2 : OTT 통합 구독 관리 및 지출 분석 사이트

- 여러 OTT 서비스의 구독 정보와 결제일 통합 관리
- 월별·연도별 구독 지출 통계 제공
- 결제 예정일 알림 기능
- 파티원 모집 및 정산 상태 관리
- Kafka / RabbitMQ / Redis Pub/Sub 기반 비동기 알림 처리 검토
- 정산 과정의 Race Condition 방지를 위한 동시성 제어 적용

**핵심 기술:** `Message Queue` · `Distributed Lock` · `Event-Driven` · `Concurrency Control`

#### 4. 후보 주제 비교

| 구분 | 고용24 채용공고 | OTT 구독 관리 |
|---|---|---|
| 핵심 기술 | Batch, ETL, 검색 최적화 | 비동기 처리, 동시성 제어 |
| 주요 문제 | 대용량 데이터 수집·검색 | 알림 트래픽·정산 동시성 |
| 주요 기술 | Spring Batch, Elasticsearch | Kafka/RabbitMQ, Redis |
| 기대 경험 | 데이터 처리 및 검색 성능 개선 | 메시지 기반 처리 및 동시성 처리 |

두 후보 모두 서비스 기능뿐만 아니라 백엔드 아키텍처 관점의 기술적 과제를 포함하도록 비교하였다.

#### 5. Troubleshooting

- 아이디어 구상 과정에서 서비스 기능에 집중하여 백엔드 기술적 도전 과제를 구체화하기 어려웠음
- 공공데이터 API 호출 제한과 대용량 트래픽 발생 지점을 초기에 고려하지 않아 아키텍처 방향이 모호했음
- 두 후보의 핵심 기술이 서로 달라 최종 주제 선정에 어려움이 있었음

---

#### 6. 핵심 정리

- 단순 CRUD를 넘어 **대용량 데이터 처리, 비동기 처리, 동시성 제어**가 포함된 프로젝트를 기획함
- 고용24 플랫폼은 **공공데이터 수집 및 검색 최적화**에 중점을 둔 주제
- OTT 구독 관리는 **비동기 알림과 정산 동시성 처리**에 중점을 둔 주제
- 프로젝트 기획 시 서비스 기능과 함께 **백엔드 기술 과제와 아키텍처를 함께 설계하는 것**이 중요함

---
