FROM maven:3.9-eclipse-temurin-8 AS build

WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:8-jre

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 18081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
