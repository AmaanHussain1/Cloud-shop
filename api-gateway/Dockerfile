FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Use a wildcard so we don't have to type the exact snapshot name!
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]