FROM openjdk:17-jdk-slim

WORKDIR /app

# Maven 의존성 복사
COPY pom.xml .
COPY src ./src

# Maven 빌드
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# JAR 파일만 복사
RUN cp target/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
