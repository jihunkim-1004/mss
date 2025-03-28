# MSS Shop API

## 구현 범위

### 1. 상품 관리 API
- 상품 등록: 브랜드, 카테고리, 가격 정보를 입력받아 상품을 등록
- 상품 수정: 상품 ID를 기반으로 상품 정보 수정
- 상품 삭제: 상품 ID를 기반으로 상품 삭제

### 2. 가격 조회 API
- 카테고리별 최저가격 조회: 각 카테고리별로 최저가격을 가진 브랜드 정보 조회
- 단일 브랜드 최저가격 조회: 모든 카테고리의 상품을 구매할 때 최저가격을 가진 브랜드 정보 조회
- 카테고리별 최저/최고가격 조회: 특정 카테고리의 최저가격과 최고가격 브랜드 정보 조회

### 3. 데이터 구조
- 브랜드: 상품의 브랜드명
- 카테고리: 상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리
- 가격: 상품의 가격 정보

## 빌드 및 실행 방법

### 환경 요구사항
- JDK 17 이상
- Gradle 8.0 이상
- Spring Boot 3.0 이상

### 빌드 방법
```bash
# 프로젝트 루트 디렉토리에서
./gradlew build
```

### 테스트 실행
```bash
# 전체 테스트 실행
./gradlew test
```

### 애플리케이션 실행

```bash
# 애플리케이션 실행
./gradlew bootRun
```

## API 엔드포인트

### 상품 관리
- POST /api/products - 상품 등록
- PUT /api/products/{id} - 상품 수정
- DELETE /api/products/{id} - 상품 삭제

### 가격 조회
- GET /api/products/category/lowest - 카테고리별 최저가격 조회
- GET /api/products/brand/lowest - 단일 브랜드 최저가격 조회
- GET /api/products/category/{category}/range - 카테고리별 최저/최고가격 조회

## 기타 정보

### 프로젝트 구조
```
src/
├── main/
│   ├── java/
│   │   └── com/mss/shop/mss/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       └── dto/
│   └── resources/
└── test/
    └── java/
        └── com/mss/shop/mss/
            ├── integration/
            └── service/
```

### 테스트 데이터
- 기본 테스트 데이터는 A, B, C 세 브랜드의 8개 카테고리 상품 정보를 포함 합니다.
- 각 브랜드별로 모든 카테고리의 상품이 존재하며, 가격은 카테고리별로 다르게 설정

### 예외 처리
- 상품 등록 시 중복된 브랜드와 카테고리 조합은 허용되지 않음
- 존재하지 않는 상품 ID로 수정/삭제 시도 시 예외 발생
- 카테고리별 조회 시 해당 카테고리의 상품이 없는 경우 예외 발생
