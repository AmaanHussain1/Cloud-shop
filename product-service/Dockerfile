# Base Image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy our compiled .jar file from our Windows machine into the Linux container
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port our app runs on
EXPOSE 8081

#The command that runs when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]