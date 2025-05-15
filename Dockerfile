FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/matdongsan-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
