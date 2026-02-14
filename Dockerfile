FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app-api/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
