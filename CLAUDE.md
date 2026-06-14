# Matdongsan-BE — Agent Guide

맛동산 백엔드(멀티모듈 Spring Boot 3.4.3 / Java 21). 긴 지식은 **brain** 에 산다 — 이 파일은 얇은 라우터다.

## Knowledge Base

지식베이스: `~/Documents/matdongsan-brain/` (Obsidian vault). 컨벤션은 `~/Documents/newoff-brain` 동일.

**Always read first:**
- `~/Documents/matdongsan-brain/00-Index/AI-Agent-Entry.md` — 읽기 순서·status 규칙·hard rules
- `~/Documents/matdongsan-brain/02-Architecture/matdongsan-be.md` — 이 서비스 stub

**Read by topic (lazy-load — 작업이 닿는 것만):**
- 코드 구조(모듈/DDD/CQRS) → `02-Architecture/patterns/Backend-Module-Structure.md`
- 인증/약관/토큰 → `03-Domain/auth/auth-domain.md`
- 회원/프로필/탈퇴/차단 → `03-Domain/user/user-domain.md`
- 제철음식/featuredFood/foodStory/공공데이터 → `03-Domain/food/food-domain.md`
- 음식 대결/투표 → `03-Domain/dish/dish-domain.md`
- 홈 → `03-Domain/home/home-domain.md`
- DB/스키마 → `03-Domain/DB-Schema.md`
- 배포/CI/장애 → `05-Runbooks/Deployment.md`
- 모르는 용어 → `03-Domain/Glossary.md`
- **현황·문제 우선순위 → `00-Index/Health-Report.md`** (복귀/대규모 작업 전 필독)
- 과거 결정 → `00-Index/Decision-Log.md`

## Project quick facts
- 모듈: `app-api`(REST), `app-admin`(빈 껍데기), `core-domain`(POJO+repo 인터페이스), `core-data`(JPA+QueryDSL+MapStruct), `core-utils`.
- DDD POJO/Entity 분리 + CQRS 네이밍, ~80% 전환(`external/opendata` 만 미완).
- soft-delete 글로벌 필터 없음 → 쿼리에 `deletedAt IS NULL` 직접. 물리 FK 없음.
- 빌드/실행: `./gradlew clean bootJar` (산출물은 `app-api/build/libs/`). 테스트 사실상 없음.

## Hard rules
- 도메인 POJO 에 JPA 어노테이션 금지(JPA 는 `core-data` 에만).
- 이 파일에 brain 내용을 복붙하지 말 것 — 링크만. 새 사실은 brain 에 기록(절차는 AI-Agent-Entry "After non-trivial changes").
- 코드와 brain 노트가 다르면 코드가 현재 진실 — 단 doc drift 를 brain 에 반영 제안.
