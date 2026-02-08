# 작업 계획서: DDD 구조 전환 및 CQRS 패턴 적용

> **상태**: 초안 (2차 - Dish 파일럿 완료 후 반영)
> **작성일**: 2026-02-07 (최종 수정: 2026-02-08)
> **영향 범위**: `app-api`, `core-domain`, `core-data`

---

## 1. 현재 구조 분석

### 1.1 현재 문제점

#### 문제 1: 도메인 엔티티와 JPA 엔티티 미분리
- `core-data`에 JPA 엔티티가 곧 도메인 모델 역할을 겸하고 있음
- JPA 어노테이션(`@Entity`, `@Column`, `@ManyToOne` 등)이 도메인 로직과 혼재
- 예: `Dish.java`의 `plusVoteCount()`는 도메인 로직이지만 JPA 엔티티 안에 존재

#### 문제 2: app-api → core-data 직접 의존
- `app-api`의 Service가 `JpaRepository`를 직접 주입받아 사용
- 영속 계층 기술(JPA, Redis)이 비즈니스 로직에 노출됨
- 예: `FoodService`가 `FoodRepository`(JpaRepository)를 직접 의존

#### 문제 3: Query/Command 미분리
- `JpaRepository` 하나로 조회와 명령을 모두 처리
- 복잡한 조회 시 `findAllByFeaturedFoodOrderByVoteCountDesc` 같은 긴 메서드명 필요
- 조회 최적화(QueryDSL 등)를 적용하기 어려운 구조

### 1.2 현재 모듈별 역할

| 모듈 | 현재 역할 |
|------|----------|
| `app-api` | Controller + Service (JpaRepository 직접 의존) |
| `core-domain` | Enum만 보유 (LoginType, FoodStoryType, TermsType, Genre) |
| `core-data` | JPA Entity + JPA Repository + Redis Entity/Repository + Converter + Config |

### 1.3 현재 엔티티 목록 (core-data)

| 도메인 | JPA 엔티티 | Base Class | 비고 |
|--------|-----------|------------|------|
| **User** | `User` | `BaseTimeEntityWithSoftDelete` | OneToOne → UserProfile, OneToMany → UserLoginCredential, UserAgreement |
| | `UserProfile` | `BaseTimeEntityWithSoftDelete` | 팩토리 메서드: createDefault(), createOauth() |
| | `UserLoginCredential` | `BaseTimeEntityWithSoftDelete` | 팩토리 메서드: createEmailLogin(), createOauthLogin() |
| | `UserAgreement` | 없음 (Builder) | ManyToOne → User, Terms |
| **Auth** | `Terms` | `BaseTimeEntity` | type(TermsType), required, active, orderNum |
| **Food** | `Food` | `BaseTimeEntity` | JSON 컬럼: seasonMonths(List), nutrients(Map) |
| | `FeaturedFood` | `BaseTimeEntity` | ManyToOne → Food, year/week/startAt/endAt |
| | `FoodStory` (abstract) | `BaseTimeEntityWithSoftDelete` | SINGLE_TABLE 상속, DiscriminatorColumn="type" |
| | `FoodStoryPlace` | FoodStory | placeName, address, naverUrl 등 |
| | `FoodStoryRecipe` | FoodStory | recipeName, ingredients, instructions |
| | `FoodStorySeasonalNote` | FoodStory | content, recordedDate |
| | `FoodStoryImage` | `BaseTimeEntityWithSoftDelete` | ManyToOne → FoodStory |
| | `FoodStoryLike` | `BaseTimeEntityWithSoftDelete` | ManyToOne → FoodStory + userId |
| | `FoodStoryReport` | `BaseTimeEntityWithSoftDelete` | ManyToOne → FoodStory + userId |
| **Dish** | `Dish` | `BaseTimeEntityWithSoftDelete` | ManyToOne → Food, FeaturedFood. 메서드: plusVoteCount() |
| | `DishVote` | `BaseTimeEntityWithSoftDelete` | ManyToOne → Dish + userId |
| | `DishVoteImage` | `BaseTimeEntityWithSoftDelete` | ManyToOne → Dish, DishVote |
| | `DishVoteImageReport` | `BaseTimeEntityWithSoftDelete` | ManyToOne → DishVoteImage + userId |
| **External** | `ExternalFoodSource` | `BaseTimeEntity` | 공공데이터 원본 |
| **Dummy** | `Author`, `Book` | `BaseTimeEntity` | 테스트/더미용 (마이그레이션 대상 아님) |

### 1.4 현재 Repository 목록 (core-data)

#### JPA Repository

| Repository | 커스텀 메서드 |
|-----------|-------------|
| `UserRepository` | (없음) |
| `UserProfileRepository` | (없음) |
| `UserLoginCredentialRepository` | `existsByEmail`, `existsByLoginTypeAndEmail`, `findByLoginTypeAndEmail` |
| `UserAgreementRepository` | (없음) |
| `TermsRepository` | `findAllByActiveTrueOrderByOrderNumAsc`, `findRequiredTermsIds` (@Query) |
| `FoodRepository` | `existsByName` |
| `FoodStoryRepository` | `findAllByFoodAndDeletedAtIsNull` (Page) |
| `FoodStoryImageRepository` | `findAllByFoodStoryAndDeletedAtIsNull` |
| `FoodStoryLikeRepository` | (없음) |
| `FoodStoryReportRepository` | (없음) |
| `FoodStoryPlaceRepository` | (없음) |
| `FoodStoryRecipeRepository` | (없음) |
| `FoodStorySeasonalNoteRepository` | (없음) |
| `FeaturedFoodRepository` | `findFirstByFoodOrderByStartAtDesc` |
| `DishRepository` | `findAllByFeaturedFoodOrderByVoteCountDesc` |
| `DishVoteRepository` | (없음) |
| `DishVoteImageRepository` | `findAllByDishAndDeletedAtIsNullOrderByCreatedAtDesc` |
| `DishVoteImageReportRepository` | (없음) |
| `ExternalFoodSourceRepository` | (없음) |
| `AuthorRepository` | (없음) |
| `BookRepository` | `findAll(Pageable)` |

#### Redis Repository

| Repository | Entity | TTL |
|-----------|--------|-----|
| `RefreshTokenRepository` | `RefreshToken` | 7일 |
| `EmailVerificationRepository` | `EmailVerification` | 5분 |
| `VerifiedEmailRepository` | `VerifiedEmail` | 10분 |

### 1.5 현재 Service → Repository 의존성

| Service | 의존하는 Repository |
|---------|-------------------|
| `AuthService` | TermsRepository, UserRepository, UserLoginCredentialRepository, UserAgreementRepository, UserProfileRepository, EmailVerificationRepository, VerifiedEmailRepository, RefreshTokenRepository |
| `FoodService` | FoodRepository, FoodStoryRepository, FoodStoryImageRepository, FeaturedFoodRepository, DishRepository |
| `FoodStoryService` | FoodRepository, FoodStoryRepository, FoodStorySeasonalNoteRepository, FoodStoryRecipeRepository, FoodStoryPlaceRepository, FoodStoryImageRepository |
| `DishService` | FoodRepository, FeaturedFoodRepository, DishRepository, DishVoteRepository, DishVoteImageRepository |
| `OauthService` | (Repository 없음 - Feign Client만 사용) |
| `FoodGenerateService` | ExternalFoodSourceRepository, FoodRepository |

---

## 2. 변경될 구조

### 2.1 모듈별 역할 변경

| 모듈 | 변경 후 역할 |
|------|-------------|
| `app-api` | Controller + Service → **core-domain의 Repository 인터페이스만 의존** |
| `core-domain` | Enum + **도메인 엔티티** + **Repository 인터페이스 (Query/Command 분리)** |
| `core-data` | **JPA Entity ({Name}Entity)** + **Repository 구현체** (Command: JPA, Query: QueryDSL) + Redis + Converter + MapStruct Mapper |

### 2.2 변경될 패키지 구조

#### core-domain (변경 후)

```
com.example.matdongsan/
├── user/
│   ├── domain/
│   │   ├── User.java                    # 도메인 엔티티 (POJO, Lombok만)
│   │   ├── UserProfile.java
│   │   └── UserLoginCredential.java
│   └── repository/
│       ├── UserCommandRepository.java    # interface
│       └── UserQueryRepository.java      # interface
├── auth/
│   ├── domain/
│   │   ├── Terms.java
│   │   ├── UserAgreement.java
│   │   ├── RefreshToken.java            # Redis 도메인 모델 (POJO)
│   │   ├── EmailVerification.java       # Redis 도메인 모델 (POJO)
│   │   └── VerifiedEmail.java           # Redis 도메인 모델 (POJO)
│   ├── enums/
│   │   ├── LoginType.java               # 기존 유지
│   │   └── TermsType.java              # 기존 유지
│   └── repository/
│       ├── TermsQueryRepository.java
│       ├── AuthCommandRepository.java
│       ├── AuthTokenCommandRepository.java  # Redis 쓰기
│       └── AuthTokenQueryRepository.java    # Redis 조회
├── food/
│   ├── domain/
│   │   ├── Food.java
│   │   ├── FeaturedFood.java
│   │   ├── FoodStory.java              # 단일 클래스 (합성 패턴, type 필드로 구분)
│   │   ├── FoodStoryDetail.java        # 타입별 상세 데이터 (합성)
│   │   ├── FoodStoryImage.java
│   │   ├── FoodStoryLike.java
│   │   └── FoodStoryReport.java
│   ├── enums/
│   │   └── FoodStoryType.java          # 기존 유지
│   └── repository/
│       ├── FoodCommandRepository.java
│       ├── FoodQueryRepository.java
│       ├── FoodStoryCommandRepository.java
│       └── FoodStoryQueryRepository.java
├── dish/
│   ├── domain/
│   │   ├── Dish.java
│   │   ├── DishVote.java
│   │   ├── DishVoteImage.java
│   │   └── DishVoteImageReport.java
│   └── repository/
│       ├── DishCommandRepository.java
│       └── DishQueryRepository.java
├── external/
│   ├── domain/
│   │   └── ExternalFoodSource.java      # 도메인 엔티티
│   └── repository/
│       ├── ExternalFoodSourceCommandRepository.java
│       └── ExternalFoodSourceQueryRepository.java
└── common/
    ├── enums/
    │   └── Genre.java                   # 기존 유지
    └── model/
        ├── PageResult.java              # 커스텀 Page 래퍼 (Spring 의존성 제거)
        └── PageQuery.java               # 커스텀 Pageable 래퍼
```

#### core-data (변경 후)

```
com.example.matdongsan/
├── user/
│   ├── entity/
│   │   ├── UserEntity.java              # JPA (기존 User → 이름 변경)
│   │   ├── UserProfileEntity.java
│   │   └── UserLoginCredentialEntity.java
│   ├── repository/
│   │   ├── UserJpaRepository.java       # JpaRepository (내부 전용)
│   │   ├── UserProfileJpaRepository.java
│   │   ├── UserLoginCredentialJpaRepository.java
│   │   ├── UserCommandRepositoryImpl.java   # core-domain 인터페이스 구현
│   │   └── UserQueryRepositoryImpl.java     # QueryDSL 구현
│   └── mapper/
│       └── UserMapper.java              # Entity ↔ Domain 변환
├── auth/
│   ├── entity/
│   │   ├── TermsEntity.java
│   │   └── UserAgreementEntity.java
│   ├── redis/
│   │   ├── RefreshTokenRedisEntity.java
│   │   ├── EmailVerificationRedisEntity.java
│   │   ├── VerifiedEmailRedisEntity.java
│   │   ├── RefreshTokenRedisRepository.java
│   │   ├── EmailVerificationRedisRepository.java
│   │   └── VerifiedEmailRedisRepository.java
│   ├── repository/
│   │   ├── TermsJpaRepository.java
│   │   ├── UserAgreementJpaRepository.java
│   │   ├── TermsQueryRepositoryImpl.java
│   │   ├── AuthCommandRepositoryImpl.java
│   │   ├── AuthTokenCommandRepositoryImpl.java
│   │   └── AuthTokenQueryRepositoryImpl.java
│   └── mapper/
│       └── AuthMapper.java
├── food/
│   ├── entity/
│   │   ├── FoodEntity.java
│   │   ├── FeaturedFoodEntity.java
│   │   ├── FoodStoryEntity.java         # abstract (JPA 상속 유지)
│   │   ├── FoodStoryPlaceEntity.java    # JPA 서브타입 유지
│   │   ├── FoodStoryRecipeEntity.java   # JPA 서브타입 유지
│   │   ├── FoodStorySeasonalNoteEntity.java
│   │   ├── FoodStoryImageEntity.java
│   │   ├── FoodStoryLikeEntity.java
│   │   └── FoodStoryReportEntity.java
│   ├── repository/
│   │   ├── FoodJpaRepository.java
│   │   ├── FeaturedFoodJpaRepository.java
│   │   ├── FoodStoryJpaRepository.java
│   │   ├── FoodStoryImageJpaRepository.java
│   │   ├── FoodStoryLikeJpaRepository.java
│   │   ├── FoodStoryReportJpaRepository.java
│   │   ├── FoodStoryPlaceJpaRepository.java
│   │   ├── FoodStoryRecipeJpaRepository.java
│   │   ├── FoodStorySeasonalNoteJpaRepository.java
│   │   ├── FoodCommandRepositoryImpl.java
│   │   ├── FoodQueryRepositoryImpl.java
│   │   ├── FoodStoryCommandRepositoryImpl.java
│   │   └── FoodStoryQueryRepositoryImpl.java
│   └── mapper/
│       └── FoodMapper.java
├── dish/
│   ├── entity/
│   │   ├── DishEntity.java
│   │   ├── DishVoteEntity.java
│   │   ├── DishVoteImageEntity.java
│   │   └── DishVoteImageReportEntity.java
│   ├── repository/
│   │   ├── DishJpaRepository.java
│   │   ├── DishVoteJpaRepository.java
│   │   ├── DishVoteImageJpaRepository.java
│   │   ├── DishVoteImageReportJpaRepository.java
│   │   ├── DishCommandRepositoryImpl.java
│   │   └── DishQueryRepositoryImpl.java
│   └── mapper/
│       └── DishMapper.java
├── external/
│   ├── entity/
│   │   └── ExternalFoodSourceEntity.java
│   ├── repository/
│   │   ├── ExternalFoodSourceJpaRepository.java
│   │   ├── ExternalFoodSourceCommandRepositoryImpl.java
│   │   └── ExternalFoodSourceQueryRepositoryImpl.java
│   └── mapper/
│       └── ExternalFoodSourceMapper.java
└── common/
    ├── config/
    │   ├── JpaConfig.java               # 기존 유지
    │   └── QueryDslConfig.java          # JPAQueryFactory Bean 등록
    ├── entity/
    │   ├── BaseTimeEntity.java          # 기존 유지
    │   └── BaseTimeEntityWithSoftDeleteEntity.java
    └── converter/
        ├── JsonMapConverter.java        # 기존 유지
        └── JsonListConverter.java       # 기존 유지
```

#### app-api (변경 후 - 의존성만 변경)

```
# 기존 구조 유지, Service의 의존성만 변경
com.example.matdongsan/
├── auth/application/service/
│   └── AuthService.java
│       - 변경 전: TermsRepository, UserRepository, ...JpaRepository 직접 의존
│       - 변경 후: TermsQueryRepository, UserCommandRepository, AuthTokenQueryRepository, AuthTokenCommandRepository 등 interface 의존
├── food/application/service/
│   ├── FoodService.java
│   │   - 변경 후: FoodQueryRepository, DishQueryRepository 등 interface 의존
│   └── FoodStoryService.java
│       - 변경 후: FoodStoryCommandRepository 등 interface 의존
└── dish/application/service/
    └── DishService.java
        - 변경 후: DishCommandRepository, DishQueryRepository 등 interface 의존
```

### 2.3 모듈 의존성 변경

```
변경 전:
app-api ──→ core-data (JpaRepository 직접 사용)
app-api ──→ core-domain (Enum만)

변경 후:
app-api ──→ core-domain (도메인 엔티티 + Repository 인터페이스)
app-api ──✕ core-data (직접 의존 제거)

core-data ──→ core-domain (Repository 인터페이스 구현)
```

> **핵심**: `app-api`의 `build.gradle.kts`에서 `core-data` 의존성을 `runtimeOnly`로 변경.
> 컴파일 타임에는 `core-domain`의 인터페이스만 참조하고, 런타임에 `core-data`의 구현체가 주입됨.

### 2.4 데이터 흐름 변경

```
변경 전:
Controller → Service → JpaRepository → JPA Entity → DB

변경 후:
Controller → Service → QueryRepository (interface, core-domain)
                          ↓ 런타임 구현체 주입
                     QueryRepositoryImpl (core-data) → QueryDSL → DB

Controller → Service → CommandRepository (interface, core-domain)
                          ↓ 런타임 구현체 주입
                     CommandRepositoryImpl (core-data) → JpaRepository → DB
```

### 2.5 모델 변환 흐름

```
[Client]         [app-api Controller]      [app-api Service]       [core-domain]        [core-data]
Request  ──→  request.toParam()  ──→  Param  ──→  Service  ──→  Repository(I/F)  ──→  RepositoryImpl
                                                     │                                      │
                                                     │         Domain  ←── MapStruct ──  JPA Entity
                                                     │            │
                                              ServiceDto.from(domain)
                                                     │
Response  ←──  Response.from(dto)  ←──  ServiceDto  ←┘
```

```
예시 (조회):
Client ← DishResponse.from(dto) ← DishServiceDto.from(dish) ← Dish ← DishMapper.toDomain(entity) ← DishEntity ← DB

예시 (생성):
Client → CreateDishParam → DishService.createDish(param) → Dish.create(...) → DishCommandRepository.save(dish) → DB
```

---

## 3. 도메인 엔티티 설계

### 3.1 설계 결정 사항

| 항목 | 결정 |
|------|------|
| **시간 필드** | createdAt, updatedAt, deletedAt **모두 포함** |
| **FoodStory 다형성** | **합성(Composition)** 패턴 사용 (상속 제거, type + FoodStoryDetail) |
| **Spring 의존성** | core-domain에서 **제거**. 커스텀 PageResult/PageQuery 사용 |
| **Dummy 엔티티** | Author, Book **삭제** |
| **External 엔티티** | ExternalFoodSource DDD 구조로 **마이그레이션** |
| **애그리거트 설계** | **방안 C**: 독립 애그리거트 + 도메인 서비스 조율 (상세: 3.7절) |
| **팩토리 메서드** | 도메인 엔티티에 `create()` 정적 팩토리 메서드 사용 (상세: 3.8절) |
| **DTO 컨벤션** | Param(입력) / ServiceDto(출력) / Response(응답) 분리 (상세: 3.9절) |
| **정적 팩토리 네이밍** | `from(객체 변환)` / `of(값 조합 생성)` (상세: 3.9절) |
| **Request → Param 변환** | **수동 변환 유지** (`request.toParam()`), MapStruct 미사용 |
| **ServiceDto 분리 전략** | **단일 ServiceDto**로 시작, 필드 차이가 커지면 분리 |

### 3.2 도메인 엔티티 vs JPA 엔티티

| 항목 | 도메인 엔티티 (core-domain) | JPA 엔티티 (core-data) |
|------|---------------------------|----------------------|
| 위치 | `core-domain/{domain}/domain/` | `core-data/{domain}/entity/` |
| 어노테이션 | Lombok만 (`@Getter`, `@Builder`) | JPA 어노테이션 전체 |
| 이름 | `Dish.java` | `DishEntity.java` |
| 역할 | 비즈니스 로직, 도메인 규칙 | DB 매핑, 영속성 |
| 참조 대상 | app-api, app-admin에서 사용 | core-data 내부에서만 사용 |
| 연관관계 | **ID 참조** (`Long foodId`) | **객체 참조** (`FoodEntity food`) |
| 시간 필드 | createdAt, updatedAt, deletedAt 직접 필드 | BaseTimeEntity 상속 |

### 3.3 도메인 엔티티 예시

```java
// core-domain/dish/domain/Dish.java
@Getter
@Builder
public class Dish {
    private Long id;
    private Long foodId;
    private Long featuredFoodId;
    private String name;
    private Integer voteCount;
    private List<DishVoteImage> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // 팩토리 메서드: 생성 로직을 도메인에 캡슐화
    public static Dish create(Long foodId, Long featuredFoodId, String name) {
        return Dish.builder()
                .foodId(foodId)
                .featuredFoodId(featuredFoodId)
                .name(name)
                .voteCount(1)
                .build();
    }

    // 도메인 로직
    public void plusVoteCount() {
        this.voteCount++;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

// core-domain/dish/domain/DishVote.java - 애그리거트 루트 (자식: DishVoteImage)
@Getter
@Builder
public class DishVote {
    private Long id;
    private Long dishId;
    private Long userId;
    private List<DishVoteImage> images;  // 자식 엔티티 (동일 생명주기)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // 팩토리 메서드: Vote + Images를 함께 생성
    public static DishVote create(Long dishId, Long userId, List<String> imageUrls) {
        DishVote vote = DishVote.builder()
                .dishId(dishId)
                .userId(userId)
                .build();
        List<DishVoteImage> images = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> DishVoteImage.create(dishId, imageUrls.get(i), i + 1))
                .toList();
        vote.images = images;
        return vote;
    }
}
```

### 3.4 FoodStory 합성 패턴 설계

JPA 엔티티는 기존 SINGLE_TABLE 상속을 유지하되, **도메인 엔티티에서는 합성 패턴으로 변환**:

```java
// core-domain/food/domain/FoodStory.java
@Getter
@Builder
public class FoodStory {
    private Long id;
    private Long foodId;
    private Long userId;
    private FoodStoryType type;          // RECIPE, PLACE, SEASONAL_NOTE
    private FoodStoryDetail detail;      // 타입별 상세 데이터
    private Integer likeCount;
    private Integer reportCount;
    private List<FoodStoryImage> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}

// core-domain/food/domain/FoodStoryDetail.java
@Getter
@Builder
public class FoodStoryDetail {
    // PLACE 필드
    private String placeName;
    private String address;
    private String naverUrl;
    private String category;

    // RECIPE 필드
    private String recipeName;
    private String ingredients;
    private String instructions;

    // SEASONAL_NOTE 필드
    private String content;
    private LocalDate recordedDate;
}
```

> **핵심**: JPA에서는 `FoodStoryPlaceEntity`, `FoodStoryRecipeEntity`, `FoodStorySeasonalNoteEntity` 상속 구조 유지.
> MapStruct Mapper에서 서브타입 → FoodStory(합성) 변환 처리.

### 3.5 커스텀 Page 래퍼

```java
// core-domain/common/model/PageResult.java
@Getter
@Builder
public class PageResult<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;

    public static <T> PageResult<T> of(List<T> content, int page, int size, long totalElements) {
        return PageResult.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .hasNext(page + 1 < (int) Math.ceil((double) totalElements / size))
                .build();
    }
}

// core-domain/common/model/PageQuery.java
@Getter
@Builder
public class PageQuery {
    private int page;
    private int size;
    private String sortBy;
    private boolean ascending;
}
```

> **변환**: core-data의 RepositoryImpl에서 `PageQuery` → Spring `Pageable`, Spring `Page` → `PageResult` 변환 처리.

### 3.6 JPA 엔티티 예시 (리네이밍)

```java
// core-data/dish/entity/DishEntity.java
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "dish")
public class DishEntity extends BaseTimeEntityWithSoftDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_food_id", nullable = false)
    private FeaturedFoodEntity featuredFood;

    private String name;
    private Integer voteCount = 0;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<DishVoteImageEntity> images = new ArrayList<>();
}
```

### 3.7 애그리거트 설계 (방안 C: 독립 애그리거트 + 도메인 서비스 조율)

#### 설계 원칙

- **생명주기가 같은 엔티티**는 하나의 애그리거트로 묶는다
- **생명주기가 다른 엔티티**는 별도 애그리거트로 분리하고 ID로만 참조한다
- 애그리거트 간 비즈니스 일관성은 **Service(도메인 서비스)**가 조율한다
- 애그리거트 루트가 자식 엔티티의 생성/삭제를 책임진다

#### Dish 도메인 애그리거트 (파일럿 적용 완료)

```
[Dish]                  ← 애그리거트 루트 (voteCount 관리)
[DishVote]              ← 애그리거트 루트 (Vote + Images 생명주기 관리)
  └─ DishVoteImage      ← DishVote의 자식 (동일 생명주기, 함께 생성/삭제)
[DishVoteImageReport]   ← 독립 엔티티 (voteImageId FK로만 참조)

[DishService]           ← 도메인 서비스: Dish ↔ DishVote 간 조율
                          예) voteDish() 시 DishVote 생성 + Dish.plusVoteCount() 조율
```

| 엔티티 | 애그리거트 | 역할 | 자식 참조 |
|--------|-----------|------|----------|
| Dish | Dish (루트) | 요리 정보, 투표 수 관리 | — |
| DishVote | DishVote (루트) | 투표, 이미지 생명주기 관리 | `List<DishVoteImage>` |
| DishVoteImage | DishVote의 자식 | 투표 이미지 | — |
| DishVoteImageReport | 독립 | 이미지 신고 | — |

> **주의**: 애그리거트를 수정(Command)할 때는 전체를 로드해야 한다. 단, 현재 DishVote는 생성만 하고 수정하지 않는 패턴(append-only)이므로 성능 이슈 없음.
> 조회(Query)는 CQRS에 의해 애그리거트를 거치지 않고 QueryRepository에서 필요한 데이터만 직접 조회.

### 3.8 도메인 엔티티 팩토리 메서드 패턴

#### 원칙

- 도메인 엔티티 생성 로직은 **도메인 엔티티 내부의 `create()` 정적 팩토리 메서드**에 캡슐화한다
- Service에서 builder를 직접 사용하지 않는다
- 생성 시 필요한 기본값, 초기화 로직을 팩토리 메서드에서 처리한다
- 애그리거트 루트의 팩토리 메서드가 자식 엔티티도 함께 생성한다

#### 예시

```java
// Service에서 사용 (변경 후)
Dish dish = Dish.create(food.getId(), featuredFood.getId(), param.getName());
DishVote vote = DishVote.create(dish.getId(), userId, param.getImageUrls());  // images도 함께 생성

// Service에서 사용 (변경 전 - 지양)
Dish dish = Dish.builder().foodId(...).featuredFoodId(...).name(...).voteCount(1).build();
```

### 3.9 DTO 레이어 컨벤션

#### 3.9.1 DTO 종류 및 역할

| 구분 | 네이밍 | 역할 | 위치 |
|------|--------|------|------|
| **Request** | `{Action}{Domain}Request` | API 요청 바인딩 | `app-api/{domain}/presentation/request/` |
| **Param** | `{Action}{Domain}Param` | Service 입력 파라미터 | `app-api/{domain}/application/dto/` |
| **ServiceDto** | `{Domain}ServiceDto` | Service 출력 (도메인 캡슐화) | `app-api/{domain}/application/dto/` |
| **Response** | `{Domain}{Detail}Response` | API 응답 | `app-api/{domain}/presentation/response/` |

#### 3.9.2 데이터 흐름

```
Client → Request → request.toParam() → Param → Service
                                                  │
                                                  │ (조회: ServiceDto 반환 / 생성·수정·삭제: void)
                                                  ↓
Client ← Response ← Response.from(dto) ← ServiceDto.from(domain) ← Domain
```

#### 3.9.3 변환 메서드 네이밍 규칙

| 변환 | 메서드 | 위치 | 의미 |
|------|--------|------|------|
| Request → Param | `toParam()` | Request에 정의 | 자기 자신을 변환 |
| Domain → ServiceDto | `from(domain)` | ServiceDto에 정의 | 다른 타입에서 변환 |
| ServiceDto → Response | `from(serviceDto)` | Response에 정의 | 다른 타입에서 변환 |
| 값 조합 → 객체 | `of(values...)` | 해당 클래스에 정의 | 여러 값으로 생성 |
| Domain 생성 | `create(...)` | Domain에 정의 | 비즈니스 규칙 포함 생성 |

> **`from` vs `of` 구분**:
> - `from(object)`: 단일 객체의 **타입 변환** (예: `FoodServiceDto.from(food)`)
> - `of(values...)`: 원시 값 / 여러 파라미터로 **조합 생성** (예: `PageResult.of(content, page, size, total)`)

#### 3.9.4 Service 반환 타입 규칙

| 반환 유형 | 사용 시점 | 예시 |
|----------|----------|------|
| **ServiceDto** | 조회 결과 반환 | `FoodServiceDto`, `PageResult<FoodStoryServiceDto>` |
| **void** | 생성·수정·삭제 커맨드 | `createDish()`, `voteDish()` |
| ~~Domain~~ | ~~사용하지 않음~~ | Service 외부에 도메인 직접 노출 방지 |
| ~~Response~~ | ~~사용하지 않음~~ | Service가 표현 계층에 의존하지 않도록 |

> **ServiceDto 분리 전략**: 도메인별 단일 ServiceDto로 시작한다 (예: `FoodServiceDto`).
> 조회 용도에 따라 필드 차이가 커지면 그때 분리한다 (예: `FoodSummaryServiceDto`).

#### 3.9.5 네이밍 변경 예시

```
변경 전                              변경 후
──────────────────────              ──────────────────────
DishServiceDto (입력)     →         CreateDishParam (입력)
DishVoteServiceDto (입력) →         VoteDishParam (입력)
SigninServiceDto (입력)   →         SigninParam (입력)
SignupServiceDto (입력)   →         SignupParam (입력)

(없음)                    →         DishServiceDto (출력, ServiceDto.from(domain))
(없음)                    →         FoodServiceDto (출력, ServiceDto.from(domain))

DishResponse.of(entity)   →         DishResponse.from(serviceDto)
FoodInfoResponse.of(food) →         FoodInfoResponse.from(foodServiceDto)
```

#### 3.9.6 Request → Param 변환 방식

**수동 변환 유지** (MapStruct 미사용):
- Request → Param은 필드 2~4개의 단순 복사이므로 `toParam()` 수동 변환이 더 적합
- MapStruct는 core-data의 Entity ↔ Domain 변환처럼 복잡한 매핑에만 사용
- app-api에 MapStruct 의존성 추가 불필요

```java
// Request
public CreateDishParam toParam() {
    return CreateDishParam.builder()
            .name(name)
            .imageUrls(imageUrls)
            .build();
}
```

---

## 4. Repository 인터페이스 설계

### 4.1 Query/Command 분리 원칙

| 구분 | 역할 | 구현 기술 | 네이밍 |
|------|------|----------|--------|
| **Command** | 생성, 수정, 삭제 | JPA (JpaRepository) | `{Domain}CommandRepository` |
| **Query** | 조회 (단건, 목록, 페이징) | QueryDSL | `{Domain}QueryRepository` |

### 4.2 Repository 인터페이스 목록

#### User 도메인

```java
// core-domain/user/repository/UserCommandRepository.java
public interface UserCommandRepository {
    User save(User user);
    void delete(User user);
}

// core-domain/user/repository/UserQueryRepository.java
public interface UserQueryRepository {
    Optional<User> findById(Long id);
    Optional<UserLoginCredential> findCredentialByLoginTypeAndEmail(LoginType loginType, String email);
    boolean existsCredentialByEmail(String email);
    boolean existsCredentialByLoginTypeAndEmail(LoginType loginType, String email);
}
```

#### Auth 도메인

```java
// core-domain/auth/repository/TermsQueryRepository.java
public interface TermsQueryRepository {
    List<Terms> findAllActiveOrderByOrderNum();
    Set<Long> findRequiredTermsIds();
    List<Terms> findAllByIds(List<Long> ids);
}

// core-domain/auth/repository/AuthCommandRepository.java
public interface AuthCommandRepository {
    UserAgreement saveAgreement(UserAgreement agreement);
}

// core-domain/auth/repository/AuthTokenCommandRepository.java
public interface AuthTokenCommandRepository {
    void saveRefreshToken(RefreshToken token);
    void deleteRefreshToken(Long userId, LoginType loginType, String jti);
    void saveEmailVerification(EmailVerification verification);
    void deleteEmailVerification(String email);
    void saveVerifiedEmail(VerifiedEmail verifiedEmail);
    void deleteVerifiedEmail(String email);
}

// core-domain/auth/repository/AuthTokenQueryRepository.java
public interface AuthTokenQueryRepository {
    Optional<RefreshToken> findRefreshToken(Long userId, LoginType loginType, String jti);
    Optional<EmailVerification> findEmailVerification(String email);
    Optional<VerifiedEmail> findVerifiedEmail(String email);
}
```

#### Food 도메인

```java
// core-domain/food/repository/FoodCommandRepository.java
public interface FoodCommandRepository {
    Food save(Food food);
}

// core-domain/food/repository/FoodQueryRepository.java
public interface FoodQueryRepository {
    Optional<Food> findById(Long id);
    boolean existsByName(String name);
}

// core-domain/food/repository/FoodStoryCommandRepository.java
public interface FoodStoryCommandRepository {
    FoodStory save(FoodStory story);          // 타입에 따라 Impl에서 분기
    FoodStoryImage saveImage(FoodStoryImage image);
}

// core-domain/food/repository/FoodStoryQueryRepository.java
public interface FoodStoryQueryRepository {
    PageResult<FoodStory> findAllByFoodId(Long foodId, PageQuery pageQuery);
    List<FoodStoryImage> findImagesByStoryId(Long storyId);
    Optional<FeaturedFood> findLatestFeaturedByFoodId(Long foodId);
}
```

#### Dish 도메인 (파일럿 적용 완료)

```java
// core-domain/dish/repository/DishCommandRepository.java
public interface DishCommandRepository {
    Dish save(Dish dish);
    /** DishVote와 내부 DishVoteImages를 함께 저장한다 (애그리거트 단위 저장). */
    DishVote saveVote(DishVote vote);
    DishVoteImageReport saveVoteImageReport(DishVoteImageReport report);
}

// core-domain/dish/repository/DishQueryRepository.java
public interface DishQueryRepository {
    Optional<Dish> findById(Long id);
    List<Dish> findAllByFeaturedFoodIdOrderByVoteCountDesc(Long featuredFoodId);
    List<DishVoteImage> findAllActiveImagesByDishId(Long dishId);
}
```

> **참고**: `saveVote()`는 DishVote 애그리거트 단위로 Vote + Images를 원자적으로 저장한다.
> `saveVoteImage()`는 제거됨 — DishVoteImage는 DishVote의 자식이므로 독립 저장하지 않는다.

---

## 5. Mapper (Entity ↔ Domain 변환) - MapStruct

### 5.1 변환 위치 및 원칙

- `core-data/{domain}/mapper/` 패키지에 위치
- **core-data 내부에서만 사용** (외부에 노출되지 않음)
- RepositoryImpl에서 JPA Entity ↔ Domain Entity 변환 시 사용
- `@Mapper(componentModel = "spring")` 으로 Spring Bean 등록

### 5.2 Mapper 작성 규칙

| 방향 | 메서드명 | 비고 |
|------|---------|------|
| JPA Entity → Domain | `toDomain(Entity)` | 연관 엔티티는 ID로 매핑 |
| Domain → JPA Entity | `toEntity(Domain)` | 연관 엔티티 필드는 `ignore = true`, RepositoryImpl에서 별도 세팅 |
| List 변환 | `toDomainList(List<Entity>)` | MapStruct가 자동 생성 |

### 5.3 Mapper 예시

```java
// core-data/dish/mapper/DishMapper.java
@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(target = "foodId", source = "food.id")
    @Mapping(target = "featuredFoodId", source = "featuredFood.id")
    Dish toDomain(DishEntity entity);

    List<Dish> toDomainList(List<DishEntity> entities);

    @Mapping(target = "food", ignore = true)
    @Mapping(target = "featuredFood", ignore = true)
    @Mapping(target = "images", ignore = true)
    DishEntity toEntity(Dish domain);
}
```

### 5.4 연관 엔티티 매핑 처리

Domain → JPA Entity 변환 시 ManyToOne 연관 엔티티는 RepositoryImpl에서 처리:

```java
// core-data/dish/repository/DishCommandRepositoryImpl.java
@Repository
@RequiredArgsConstructor
public class DishCommandRepositoryImpl implements DishCommandRepository {

    private final DishJpaRepository dishJpaRepository;
    private final FoodJpaRepository foodJpaRepository;
    private final FeaturedFoodJpaRepository featuredFoodJpaRepository;
    private final DishMapper dishMapper;

    @Override
    public Dish save(Dish dish) {
        DishEntity entity = dishMapper.toEntity(dish);
        // 연관 엔티티는 Mapper가 ignore한 필드를 여기서 세팅
        entity.setFood(foodJpaRepository.getReferenceById(dish.getFoodId()));
        entity.setFeaturedFood(featuredFoodJpaRepository.getReferenceById(dish.getFeaturedFoodId()));
        return dishMapper.toDomain(dishJpaRepository.save(entity));
    }
}
```

---

## 6. 기술 결정 사항

### 6.1 Query 기술: QueryDSL (확정)

**선택 근거**:
- JPA 엔티티 기반 Q클래스로 기존 JPA 생태계와 높은 친화성
- Spring Data JPA 통합이 우수하여 기존 프로젝트 구조에 자연스럽게 적용
- JPA 엔티티 변경 시 Q클래스가 APT로 자동 반영되어 유지보수 용이
- 학습 곡선이 낮고 Spring 커뮤니티에서 레퍼런스 풍부

**필요 의존성** (core-data/build.gradle.kts):
```kotlin
// QueryDSL
implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")
annotationProcessor("jakarta.annotation:jakarta.annotation-api")
annotationProcessor("jakarta.persistence:jakarta.persistence-api")
```

**설정**:
- `JPAQueryFactory` Bean 등록 (core-data의 config에 추가)
- Q클래스 생성 경로: `build/generated/sources/annotationProcessor`

### 6.2 모델 변환: MapStruct (확정)

**선택 근거**:
- 컴파일 타임 코드 생성으로 리플렉션 없이 높은 성능
- 인터페이스만 정의하면 보일러플레이트 코드 최소화
- 컴파일 타임 타입 체크로 매핑 오류 조기 발견
- 커스텀 매핑(@Mapping, @AfterMapping 등)으로 복잡한 변환도 대응 가능

**필요 의존성** (core-data/build.gradle.kts):
```kotlin
// MapStruct
implementation("org.mapstruct:mapstruct:1.6.3")
annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
// Lombok + MapStruct 함께 사용 시 필요
annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
```

**주의사항**:
- Lombok과 함께 사용 시 `annotationProcessor` 순서 중요 (lombok → lombok-mapstruct-binding → mapstruct-processor)
- ManyToOne 등 연관 엔티티 매핑 시 `@Mapping(target = "foodId", source = "food.id")` 활용
- 도메인 → JPA Entity 변환 시 연관 엔티티 객체는 `@Context`나 별도 파라미터로 전달

**Mapper 인터페이스 예시** (MapStruct 적용):
```java
// core-data/dish/mapper/DishMapper.java
@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(target = "foodId", source = "food.id")
    @Mapping(target = "featuredFoodId", source = "featuredFood.id")
    Dish toDomain(DishEntity entity);

    List<Dish> toDomainList(List<DishEntity> entities);

    @Mapping(target = "food", ignore = true)
    @Mapping(target = "featuredFood", ignore = true)
    @Mapping(target = "images", ignore = true)
    DishEntity toEntity(Dish domain);
}
```

---

## 7. 마이그레이션 전략

### 7.1 단계별 진행 순서

#### Phase 1: 기반 작업
1. core-domain에 커스텀 PageResult/PageQuery 클래스 생성
2. core-domain에 도메인 엔티티 클래스 생성 (FoodStory는 합성 패턴)
3. core-domain에 Repository 인터페이스 (Query/Command) 정의
4. core-data에 QueryDSL + MapStruct 의존성 추가 및 설정 (QueryDslConfig 포함)
5. core-data에 MapStruct Mapper 인터페이스 생성 (FoodStory 합성 변환 포함)
6. core-domain의 build.gradle.kts에서 Spring Data 의존성 제거

#### Phase 2: core-data 변환
5. JPA 엔티티 이름 변경 (`{Name}` → `{Name}Entity`)
6. core-data에 Repository 구현체 생성 (CommandRepositoryImpl, QueryRepositoryImpl)
7. 기존 JpaRepository는 내부 전용으로 유지 (Impl에서만 사용)

#### Phase 3: app-api 전환
8. Service의 의존성을 core-domain 인터페이스로 교체
9. Service에서 도메인 엔티티 사용하도록 변경
10. DTO 컨벤션 적용 (ServiceDto → Param 리네이밍, ServiceDto 출력 클래스 생성, Response.from() 변경)
11. Service 반환 타입을 ServiceDto로 변경 (Response 직접 반환 제거)
12. build.gradle.kts 의존성 변경 (core-data → runtimeOnly)

#### Phase 4: 검증 및 정리
13. 컴파일 및 테스트
14. Dummy 엔티티(Author, Book) 및 관련 코드 삭제
15. 사용하지 않는 코드 정리

### 7.2 도메인별 진행 순서

> 의존성이 적은 도메인부터 순차적으로 진행

1. **Auth** (Terms, UserAgreement, Redis 엔티티) - 비교적 독립적
2. **User** (User, UserProfile, UserLoginCredential)
3. **Food** (Food, FeaturedFood, FoodStory 계열) - 가장 복잡
4. **Dish** (Dish, DishVote, DishVoteImage 계열) - Food 의존

---

## 8. 리스크 및 고려사항

### 8.1 주의 사항
- **FoodStory MapStruct 변환 복잡도**: JPA 상속(3개 서브타입) → 도메인 합성(단일 클래스) 변환이 가장 복잡한 매핑. MapStruct의 `@AfterMapping` 또는 커스텀 메서드 활용 필요
- **JPA 연관관계 매핑**: Domain Entity에서는 ID 참조로, JPA Entity에서는 객체 참조로 분리. MapStruct에서 `@Mapping(target = "foodId", source = "food.id")` 패턴 활용
- **PageResult ↔ Page 변환**: core-data의 RepositoryImpl에서 Spring Page → 커스텀 PageResult 변환 로직 필요. app-api의 기존 PageResponse와의 연동도 확인
- **Flyway 마이그레이션**: DB 스키마 변경은 없으므로 영향 없음
- **Redis 엔티티**: core-domain에 순수 POJO 도메인 모델, core-data에 @RedisHash 엔티티. MapStruct로 변환
- **QueryDSL Q클래스 이름**: 엔티티 리네이밍 시 Q클래스도 `QDishEntity` 형태로 변경됨. QueryDSL 쿼리 코드에 반영 필요
- **Dummy 엔티티 삭제**: Author, Book 및 관련 Repository, 테스트 코드 함께 정리

### 8.2 영향받지 않는 범위
- `core-utils`: 예외 처리, 응답 래퍼, S3, 이메일 등 변경 없음
- `app-admin`: 미구현 상태이므로 영향 없음
- API 스펙: Controller/DTO 변경 없음 (외부 인터페이스 유지)
- DB 스키마: 테이블 구조 변경 없음

---

## 9. TODO (상세 내용 추가 예정)

### 설계 결정 (완료)

- [x] Query 기술 최종 결정 → **QueryDSL 확정**
- [x] Mapper 방식 최종 결정 → **MapStruct 확정**
- [x] core-domain Spring 의존성 → **커스텀 PageResult/PageQuery 사용**
- [x] 도메인 엔티티 시간 필드 → **createdAt, updatedAt, deletedAt 모두 포함**
- [x] FoodStory 다형성 → **합성(Composition) 패턴** (JPA는 상속 유지, 도메인은 단일 클래스)
- [x] Dummy 엔티티 → **삭제**, External 엔티티 → **DDD 구조로 마이그레이션**
- [x] 애그리거트 설계 → **방안 C** (독립 애그리거트 + 도메인 서비스 조율)
- [x] 도메인 팩토리 메서드 → **create() 정적 팩토리 메서드** 패턴
- [x] DTO 네이밍 컨벤션 → **Param(입력) / ServiceDto(출력) / Response(응답)**
- [x] 정적 팩토리 네이밍 → **from(타입 변환) / of(값 조합 생성)**
- [x] Request → Param 변환 → **수동 변환 유지** (MapStruct 미사용)
- [x] Service 반환 타입 → **ServiceDto 반환** (Domain/Response 직접 반환 금지)
- [x] ServiceDto 분리 전략 → **단일 ServiceDto로 시작**, 필요 시 분리

### 파일럿 (완료)

- [x] Dish 도메인 파일럿 전환 (도메인 엔티티, Repository I/F, 구현체, MapStruct Mapper, Service 마이그레이션)

### 남은 작업

- [ ] Dish 도메인: DTO 컨벤션 적용 (ServiceDto → Param 리네이밍, ServiceDto 출력 추가, Response.from() 변경)
- [ ] Auth 도메인 전환
- [ ] User 도메인 전환
- [ ] Food 도메인 전환
- [ ] External 도메인 전환
- [ ] Dummy 엔티티(Author, Book) 삭제
- [ ] app-api build.gradle.kts: core-data → runtimeOnly
- [ ] 각 도메인 엔티티 상세 필드 설계
- [ ] 각 Repository 인터페이스 상세 메서드 시그니처 확정
- [ ] 테스트 전략
