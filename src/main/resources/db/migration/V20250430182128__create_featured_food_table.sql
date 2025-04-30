-- 맛동산 특집 제철음식 테이블
CREATE TABLE featured_foods
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY, -- 특집 ID
    food_id    BIGINT   NOT NULL,                 -- 제철음식 ID
    year       INT      NOT NULL,                 -- 년도
    week       INT      NOT NULL,                 -- 주차
    start_at   DATETIME NOT NULL,                 -- 공개일시
    end_at     DATETIME NOT NULL,                 -- 마감일시
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (food_id) REFERENCES food (id)
);