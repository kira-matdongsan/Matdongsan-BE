-- 농림축산식품 공공데이터 포털
-- 제철 농산물 상세정보 API (https://data.mafra.go.kr/opendata/data/indexOpenDataDetail.do?data_id=20171128000000000925)
-- 원본 데이터 저장용 테이블
CREATE TABLE external_food_source
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id      BIGINT NOT NULL, -- 식품번호
    name             VARCHAR(255), -- 품목명
    month_label      VARCHAR(10), -- 월별
    season_months    VARCHAR(40), -- 월별농식품
    category         VARCHAR(255), -- 품목분류
    regions          TEXT, -- 주요 산지
    harvest_period   VARCHAR(20), -- 생산시기
    varieties        VARCHAR(255), -- 주요 품종
    benefits         TEXT, -- 효능
    buying_tips      TEXT, -- 구입요령
    cooking_tips     TEXT, -- 조리법
    preparation_tips TEXT, -- 손질요령
    detail_url       TEXT, -- 상세페이지 URL
    image_url        TEXT, -- 이미지 URL
    published_at     VARCHAR(8), -- 등록일
    created_at       DATETIME,
    updated_at       DATETIME
);