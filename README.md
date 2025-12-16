# VoucherView

스포츠 바우처 시설·강좌·리뷰·커뮤니티를 제공하는 Spring Boot 백엔드입니다. MyBatis로 MySQL을 조회하고, 카카오 OAuth → JWT 인증 흐름과 네이버 길찾기 프록시를 포함합니다.

## 기술 스택
- Java 17, Spring Boot 3.5 (Web, Security, Validation, Thymeleaf)
- MyBatis + MySQL, JTS(위경도), JJWT
- OAuth2(Kakao), Springdoc OpenAPI, RestTemplate
- Gradle, JUnit 5, Mockito/MockMvc

## 주요 기능
- **시설**: 검색/정렬/거리 필터(`GET /api/facilities`), 상세(`/{id}`), 인기 Top 3(`favorite`), 시설별 강좌 조회.
- **강좌**: 전체/시설별 목록, 단건 조회.
- **리뷰**: 작성/수정/삭제, 시설별·사용자별 조회, 평균 별점·개수 집계.
- **찜**: 시설 찜 추가/삭제, 상태/카운트 확인, 내 찜 목록.
- **커뮤니티**: 게시글 CRUD, 카테고리/키워드 검색, 추천/비추천, 댓글 CRUD.
- **메타데이터**: 지역·종목 목록/계층 구조 제공 (필터 옵션용).
- **길찾기**: 네이버 지도 Driving API 프록시(`GET /api/directions/driving`).
- **인증**: 카카오 로그인 → JWT 발급, `JwtAuthenticationFilter` + `@LoginUser`로 사용자 ID 주입.

## 디렉터리 구조
```
.                                      
├── build.gradle, settings.gradle, Dockerfile
├── src/main/java/inu/voucherview
│   ├── VoucherViewApplication.java
│   ├── annotation/         # @LoginUser 커스텀 어노테이션
│   ├── config/             # JWT 필터, CORS, Swagger, 인자 리졸버
│   ├── controller/         # REST 엔드포인트 (시설/강좌/리뷰/게시글/찜 등)
│   ├── domain/             # Facility, Course, Post, Review 등 도메인 모델
│   ├── dto/                # 요청/응답 DTO
│   ├── mapper/             # MyBatis 매퍼 인터페이스
│   ├── response/           # 목록 응답 래퍼 + 페이징
│   ├── service/            # 도메인 서비스 구현체
│   └── util/               # JWT, 페이징, CSV 파서, 위치 파서
├── src/main/resources
│   ├── application.properties
│   └── mapper/*.xml        # MyBatis SQL 정의
└── src/test/java/inu/voucherview
    ├── controller/         # MockMvc 기반 컨트롤러 테스트
    └── service/            # Mockito 기반 서비스 단위 테스트
```

## API 한눈에 보기
- **시설**: `GET /api/facilities`(검색/정렬/필터), `GET /api/facilities/{id}`, `GET /api/facilities/{id}/courses`, `GET /api/facilities/favorite`
- **강좌**: `GET /api/courses`, `GET /api/courses/{id}`
- **메타데이터**: `GET /api/metadata/filters`, `/regions`, `/regions/{province}/cities`, `/sports`
- **리뷰**: `POST /api/reviews`, `GET /api/reviews/facility/{facilityId}`, `GET /api/reviews/user/{userId}`, `PUT/DELETE /api/reviews/{id}`, `GET /api/reviews/facility/{facilityId}/rating`
- **찜**: `POST/DELETE /api/favorites/{facilityId}`, `GET /api/favorites/{facilityId}/status`, `GET /api/favorites`, `GET /api/favorites/facility/{facilityId}/count`
- **커뮤니티**: `POST/GET /api/posts`, `GET /api/posts/{id}`, `PUT/DELETE /api/posts/{id}`, `POST/DELETE /api/posts/{id}/vote`, `GET /api/posts/{id}/comments`
- **댓글**: `POST /api/posts/{postId}/comments`, `PUT/DELETE /api/comments/{id}`
- **길찾기**: `GET /api/directions/driving?start=lng,lat&goal=lng,lat&option=traoptimal`
- **인증**: `/oauth/kakao`(인가 URL), `/login/oauth2/code/kakao`(리다이렉트), `/oauth/kakao/token`, `/oauth/kakao/access` → JWT 발급

JWT가 필요한 API는 `Authorization: Bearer <token>` 헤더를 보내면 요청 속성에 `userId`가 설정되고 `@LoginUser` 파라미터로 주입됩니다.




