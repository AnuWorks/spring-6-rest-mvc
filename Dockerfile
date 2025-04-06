FROM openjdk:23-jdk-slim AS build
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/spring-6-rest-mvc-0.0.1-SNAPSHOT.jar spring6restmvc.jar

# stage 2
FROM gcr.io/distroless/java21-debian12
COPY --from=build spring6restmvc.jar spring6restmvc.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar spring6restmvc.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar spring6restmvc.jar
