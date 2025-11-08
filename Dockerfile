FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 산출물 복사 (build/libs 안의 첫 번째 JAR을 app.jar로)
COPY build/libs/*.jar /app/app.jar
COPY docker/app-entrypoint.sh /app/app-entrypoint.sh
RUN chmod +x /app/app-entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/bin/bash", "/app/app-entrypoint.sh"]
