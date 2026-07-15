FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/app-1.0.0.jar app.jar
ENTRYPOINT ["sh", "-c", "java -Xmx400m -jar app.jar --server.port=${PORT:-10000}"]
