-- 제철음식 테이블
CREATE TABLE food
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY, -- 제철음식 ID
    name             VARCHAR(255) NOT NULL,             -- 제철음식명
    english_name     VARCHAR(255),                      -- 제철음식명 영문
    is_featured      BOOLEAN DEFAULT FALSE,             -- 공개 여부
    last_featured_at DATETIME,                          -- 마지막 공개일 (featured_food.start_at)
    image_url        TEXT,                              -- 대표 이미지
    subtitle         VARCHAR(255),                      -- 소제목
    description      TEXT,                              -- 설명
    season_months    JSON,                              -- 제철시기
    regions          TEXT,                              -- 주요 산지
    benefits         TEXT,                              -- 효능
    buying_tips      TEXT,                              -- 구입요령
    preparation_tips TEXT,                              -- 손질요령
    nutrients        JSON,                              -- 영양성분
    created_at       DATETIME,
    updated_at       DATETIME
);