# Build stage
FROM --platform=linux/amd64 gradle:8.5-jdk17 AS build

WORKDIR /app

# Gradle 캐시를 활용하기 위해 먼저 의존성만 다운로드
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY . .
RUN gradle clean bootJar --no-daemon

# Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 환경변수 설정 (기본값, 실행 시 override 가능)
ENV SPRING_PROFILES_ACTIVE=prod

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
