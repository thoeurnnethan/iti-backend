# First stage: Build the application with Maven
FROM maven:3.8.5-openjdk-11 AS builder

# Copy the application source code to the container
COPY --chown=maven:maven . /home/maven/thesis

# Set the working directory
WORKDIR /home/maven/thesis

# Build the application using Maven
RUN mvn clean package -DskipTests

# Second stage: Create a lightweight image with the built application
FROM openjdk:11-jre-slim AS finalApp

# Set the working directory
WORKDIR /thesis

# Copy the JAR file from the first stage
COPY --from=builder /home/maven/thesis/target/thesis-0.0.1-SNAPSHOT.jar /thesis

# Expose the application port
EXPOSE 8090

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "/thesis/thesis-0.0.1-SNAPSHOT.jar"]

