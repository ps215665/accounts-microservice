FROM openjdk:17-jdk-slim

LABEL version="1.0"
LABEL description="Accounts Microservice image"

COPY build/libs/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
