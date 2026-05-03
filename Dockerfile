FROM eclipse-temurin:21-jdk-ubi10-minimal AS build

# Enable preview features
ENV JAVA_OPTS="--enable-preview"

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY ./backend/pom.xml .

ENV HTTP_PROXY="http://host.docker.internal:3128"
ENV HTTPS_PROXY="http://host.docker.internal:3128"
ENV http_proxy="http://host.docker.internal:3128"
ENV https_proxy="http://host.docker.internal:3128"
ENV MAVEN_OPTS="-Dhttp.proxyHost=host.docker.internal -Dhttp.proxyPort=3128 -Dhttps.proxyHost=host.docker.internal -Dhttps.proxyPort=3128 --enable-preview"

# Download dependencies and cache them
#RUN wget  https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.tar.gz
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY ./backend/src ./src

# Compile and package the application
RUN ./mvnw package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -B -V


# Use the official JDK 21 image as the base image for the runtime stage
FROM alpine:latest
RUN apk add --no-cache openjdk21

# Enable preview features
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager --enable-preview"

# Set the working directory
WORKDIR /app

# Copy the build artifacts from the build stage
#COPY --from=build /app/target/quarkus-app/quarkus-run.jar /app/app.jar
COPY --from=build /app/target/quarkus-app/lib/ /app/lib/
COPY --from=build /app/target/quarkus-app/*.jar /app/
COPY --from=build /app/target/quarkus-app/app/ /app/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /app/quarkus/

# Set the entrypoint and command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/quarkus-run.jar"]