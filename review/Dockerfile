# 1. 베이스 이미지를 설정
# OpenJDK 17 이미지 사용
FROM eclipse-temurin:17-jdk

# 2. JAR 파일 경로를 빌드 아규먼트로 받음
ARG JAR_FILE=build/libs/*.jar

# 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo Asia/Seoul > /etc/timezone

# 3. JAR 파일을 컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 4. 컨테이너 포트 정의(옵션)
EXPOSE 19106

ENV SPRING_PROFILES_ACTIVE=prod

# 5. 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app.jar"]
