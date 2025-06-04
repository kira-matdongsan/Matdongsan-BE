# 🍱 Matdongsan BE
맛동산 프로젝트는 맛있고 신선한 제철 음식을 추천하고 사람들과 함께 공유할 수 있는 커뮤니티 서비스입니다.  
Matdongsan BE는 해당 프로젝트의 백엔드 레포지토리로, Spring Boot 기반의 REST API 서버를 구성하고 있으며, 데이터 수집부터 게시물 작성, 사용자 인증까지 다양한 기능을 제공합니다.
> 🚀 인프라 구성 및 DB 마이그레이션은 별도 레포지토리 👉 [Matdongsan-Infra](https://github.com/kira-matdongsan/Matdongsan-Infra)

<br/>

## 📖 목차

- 🛠️ 기술 스택 및 도구
- 📂 프로젝트 구조
- 🚀 주요 기능
- 🔄 CI/CD 전략 (GitHub Actions)
- 🌱 API 문서 (Swagger)
- 💻 로컬 개발 환경 설정
- 🗃️ 연관 레포지토리

<br/>

## 🛠️ 기술 스택 및 도구

### Backend

- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- OpenFeign
- MySQL (AWS RDS)

### Devops

- Docker, Docker Hub, AWS ECR
- AWS EC2, RDS, S3, Route53
- Flyway (DB 마이그레이션)
- Terraform (인프라 코드화)
- GitHub Actions (CI/CD)

<br/>

## 📂 프로젝트 구조

```bash
matdongsan-api/
├── .github/workflows/          # GitHub Actions 워크플로
│   └── deploy-api.yml          # API 배포
│
├── src/
│   ├── main/
│   │   ├── java/com/example/matdongsan/
│   │   │   ├── common/               # 공통 응답, 예외 처리
│   │   │   ├── config/               # 설정 (Swagger, CORS 등)
│   │   │   ├── controller/           # REST API Controller
│   │   │   ├── domain/               # 도메인 엔티티
│   │   │   ├── external/             # 서드파티 연동 관련
│   │   │   │   └── opendata/         # 공공데이터 연동
│   │   │   ├── repository/           # JPA Repository
│   │   │   └── service/              # 비즈니스 로직
│   │   └── resources/
│   │       ├── application.yml       # 공통 설정
│   │       ├── application-dev.yml   # 개발 환경 
│   │       └── application-local.yml # 로컬 테스트 환경 (gitignore)
│   └── test/                         # 테스트 코드
│
├── build.gradle.kts           # Gradle 설정
├── Dockerfile                 # Docker 이미지 빌드용
├── README.md
└── ...
```

<br/>

## 🚀 주요 기능
- 구현 중..

<br/>

## 🔁 CI/CD 전략 (GitHub Actions)

| 이름                               | 트리거                          | 기능                            |
|----------------------------------|------------------------------|-------------------------------|
| 🚀 Deploy Matdongsan API to EC22 | `develop` 브랜치 `push` + 수동 실행 | Docker 이미지 빌드, ECR 푸시, EC2 배포 |
| 🔔 Discord Webhook 연동            | 위 워크플로에 포함                   | 배포 시작 및 성공/실패 Discord 알림      |

<br/>

## 🌱 API 문서 (Swagger)

- Swagger UI로 API를 편리하게 확인할 수 있습니다.
- [🍱 Matdongsan API Swagger UI](https://mds.hyeinisfree.me/swagger-ui/index.html)

<br/>

## 💻 로컬 개발 환경 설정

### 요구 사항

- Java 21
- Docker
- MySQL

### 실행 방법

1. **Repository Clone**

```bash
git clone https://github.com/kira-matdongsan/Matdongsan-BE.git
cd Matdongsan-BE
```

2. **의존성 설치 및 JAR 빌드**

```bash
./gradlew clean bootJar
```

3. **로컬 MySQL 데이터베이스 생성**

```sql
CREATE DATABASE matdongsan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **.env 또는 환경변수 설정**

```bash
SPRING_PROFILES_ACTIVE=local
DATABASE_URL=jdbc:mysql://localhost:3306/matdongsan
DATABASE_USERNAME=root
DATABASE_PASSWORD=yourpassword
OPENDATA_API_KEY=yourkey
```

5. **(선택 1) 직접 실행**

```bash
java -jar build/libs/matdongsan-0.0.1-SNAPSHOT.jar
```

6. **(선택 2) Docker로 실행**

```bash
docker build -t matdongsan-api .
docker run -d -p 8080:8080 --env-file .env matdongsan-api
```

## 🗃️ 연관 레포지토리

| Repository                                                              | 설명                                                 |
|-------------------------------------------------------------------------|----------------------------------------------------|
| [Matdongsan-Infra](https://github.com/kira-matdongsan/Matdongsan-Infra) | Terraform 및 Flyway 기반의 인프라, 데이터베이스 마이그레이션 관리 레포지토리 |
