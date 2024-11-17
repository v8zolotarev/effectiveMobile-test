FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/test-for-UnitBean-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080