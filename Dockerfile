FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

RUN chmod 777 mvnw

RUN ./mvnw package -DskipTests

CMD ["sh", "-c", "java -jar target/*.jar"]