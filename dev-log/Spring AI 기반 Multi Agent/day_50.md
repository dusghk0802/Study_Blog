# 50일차

## AWS EC2를 활용한 Spring Boot 백엔드 서버 배포

📌 학습일 : 2026.09.22

📌 학습 내용 : Cloud Computing, AWS Free Tier, EC2, Security Group, Elastic IP, OpenJDK 17, Git Clone, Gradle Build, Spring Boot 배포

---

#### 1. AWS 및 클라우드 컴퓨팅 개념 이해

```text
[클라우드 컴퓨팅]
컴퓨터 자원(서버, 스토리지, DB 등)을 필요한 만큼 원격으로 빌려서 사용하는 기술

[주요 AWS 서비스]
- EC2 : 원격으로 접속해 사용하는 가상 서버
- RDS : 관계형 데이터베이스 관리 서비스
- S3 : 안전하고 효율적인 파일 저장용 스토리지
- Route 53 / ELB / CloudFront 등
```

클라우드 컴퓨팅의 기본 개념과 높은 시장 점유율을 가진 AWS(Amazon Web Services)의 특징을 학습하였다.

애플리케이션 주 사용자와 물리적 거리가 가까운 **아시아 태평양 (서울) 리전(ap-northeast-2)**을 선택하여 인프라를 구성해야 함을 확인하였다.

#### 2. AWS 프리 티어 계정 생성

AWS 웹사이트에서 12개월 동안 일부 무료 서비스를 제공하는 프리 티어 계정을 생성하였다.

이메일 인증, 사용자 정보 입력, 결제 수단 등록, SMS 본인 인증 및 무료 Support 플랜 선택 과정을 통해 계정 가입을 완료하였다.

#### 3. EC2 인스턴스 생성 및 보안 그룹(Security Group) 설정

```text
- 인스턴스 이름 : dev-instagram-server
- OS 이미지 : Ubuntu Server 24.04 LTS (HVM)
- 인스턴스 유형 : t2.micro (프리 티어)
- 키 페어 : 키 페어 없이 진행 (또는 .pem 키 활용)
- 스토리지 : EBS 30 GiB (gp3)
```

AWS EC2(Elastic Compute Cloud) 가상 서버인 인스턴스를 생성하였다.

인바운드(Inbound) 보안 그룹 규칙을 설정하여 **22번 포트(SSH 원격 접속)** 및 **80번 포트(HTTP Web Traffic, 위치 무관 0.0.0.0/0)** 접속을 허용하였다.

#### 4. 고정 IP(탄력적 IP / Elastic IP) 할당 및 연결

```text
[퍼블릭 IP] : 인스턴스 재시작 시 IP 주소가 변동됨 -> 서비스 중단 원인
[탄력적 IP] : 인스턴스를 재시작해도 바뀌지 않는 고정된 정적 IPv4 주소
```

기본 퍼블릭 IP는 인스턴스 재부팅 시 주소가 변경되므로, 바뀌지 않는 고정 IP인 **탄력적 IP(Elastic IP)**를 할당받아 EC2 인스턴스에 연결하였다.

인스턴스 중지 및 재시작 후에도 IP가 유지되는 것을 실습으로 확인하였다.

#### 5. Ubuntu 서버 환경 설정 (JDK 17)

```bash
ubuntu@ip-172-31-43-197:~$ sudo apt update
ubuntu@ip-172-31-43-197:~$ sudo apt install openjdk-17-jdk -y
ubuntu@ip-172-31-43-197:~$ java -version
```

EC2 인스턴스 콘솔에 원격 접속한 후, Spring Boot 3.x 실행을 위한 OpenJDK 17을 설치하고 버전을 확인하였다.

#### 6. GitHub 프로젝트 Clone 및 설정 파일 작성

```bash
ubuntu@ip-172-31-43-197:~$ git clone https://github.com/JSCODE-EDU/ec2-spring-boot-sample.git
ubuntu@ip-172-31-43-197:~$ cd ec2-spring-boot-sample/src/main/resources
ubuntu@ip-172-31-43-197:~/.../resources$ vi application.yml
```

```yaml
server:
  port: 80
```

GitHub에서 Spring Boot 프로젝트 소스코드를 서버로 복사(Git Clone)하였다.

`vi` 편집기를 사용하여 HTTP 기본 포트인 **80번 포트**로 서버가 작동하도록 `src/main/resources/application.yml` 파일을 새로 생성하였다.

#### 7. Gradle 빌드 및 JAR 파일 생성

```bash
ubuntu@ip-172-31-43-197:~/ec2-spring-boot-sample$ ./gradlew clean build
ubuntu@ip-172-31-43-197:~/ec2-spring-boot-sample$ cd build/libs
ubuntu@ip-172-31-43-197:~/ec2-spring-boot-sample/build/libs$ ls
```

프로젝트 루트 디렉터리로 이동하여 `./gradlew clean build` 명령어로 기존 빌드를 정리하고 실행 가능한 애플리케이션을 빌드하였다.

`build/libs/` 경로로 이동하여 실행용 `.jar` 파일 생성 유무를 확인하였다.

#### 8. Spring Boot 서버 실행 및 브라우저 접속 확인

```bash
ubuntu@ip-172-31-43-197:~/ec2-spring-boot-sample/build/libs$ sudo java -jar ec2-spring-boot-sample-0.0.1-SNAPSHOT.jar
```

`sudo java -jar` 명령어를 통해 80번 포트로 Spring Boot 애플리케이션 서버를 실행시켰다.

웹 브라우저에 할당받은 탄력적 IP 주소(`http://<Public IPv4 주소>`)를 입력하여 서버가 외부 인터넷 망에서 정상적으로 작동하는지 확인하였다.

#### 9. EC2 자원 정리 및 종속성 해제

실습 완료 후 미사용 요금 청구를 방지하기 위해 사용했던 EC2 인스턴스를 종료(Terminate)하고, 할당받았던 탄력적 IP를 해제 및 삭제(Release)하였다.

---

#### 핵심 정리

- 클라우드 컴퓨팅은 물리 서버 없이 필요에 따라 원격 컴퓨팅 자원을 대여 및 관리할 수 있게 해준다.
- AWS EC2를 생성할 때는 서비스 대상 영역과 가까운 **서울 리전(ap-northeast-2)** 선택이 필수적이다.
- EC2 인스턴스의 트래픽 통제는 **보안 그룹(Security Group)**의 인바운드/아웃바운드 규칙으로 관리한다.
- **탄력적 IP(Elastic IP)**를 부여해야 EC2 중지/재시작에 따른 IP 변동 문제를 막을 수 있다.
- Linux(Ubuntu) 환경에서 **Git Clone -> application.yml 설정 -> Gradlew Build -> java -jar 실행**으로 이어지는 백엔드 서버 배포 전체 파이프라인 프로세스를 이해하였다.

---

<p align="center">
  <img src="training/Ai-Service-Deployment/2026.09.22/day_50_1.png" alt="day_50_1" width="700">
</p>


