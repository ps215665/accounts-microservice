FROM openjdk:17-jdk-slim

LABEL version="1.0"
LABEL description="Accounts Microservice image"
RUN apt-get update && apt-get install -y curl bash && rm -rf /var/lib/apt/lists/*
COPY build/libs/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
