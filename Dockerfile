### Stage 1: Build the Native Application
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS builder

WORKDIR /code

# TENTA COPIAR O SRC PRIMEIRO
COPY src /code/src

# ADICIONA UM COMANDO PARA VERIFICAR SE COPIOU
RUN echo "Listando conteúdo de /code:" && ls -la /code && echo "Listando conteúdo de /code/src:" && ls -la /code/src

# Comente o resto por enquanto para testar
# COPY --chown=quarkus:quarkus mvnw /code/mvnw
# COPY --chown=quarkus:quarkus .mvn /code/.mvn
# COPY --chown=quarkus:quarkus pom.xml /code/
# USER quarkus
# WORKDIR /code
# RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.6.1:go-offline
# RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true -DskipTests

### Stage 2: Create the runtime image
# FROM quay.io/quarkus/quarkus-micro-image:1.0
# WORKDIR /work/
# COPY --from=builder /code/target/*-runner /work/application
# EXPOSE 8080
# USER 1001
# CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]


#### Stage 1: Build the Native Application
## Use the Quarkus builder image containing GraalVM/Mandrel
#FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS builder
#
## Copy only necessary files first to leverage Docker cache
#COPY --chown=quarkus:quarkus mvnw /code/mvnw
#COPY --chown=quarkus:quarkus .mvn /code/.mvn
#COPY --chown=quarkus:quarkus pom.xml /code/
#
## Change user/workdir for security and consistency
#USER quarkus
#WORKDIR /code
#
## Download dependencies (cached layer)
## Use a plugin version explicitly for reproducibility
#RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.6.1:go-offline
#
## Copy your source code
#COPY src /code/src
#
## Build the native executable
## -Pnative enables the native profile in pom.xml
## -Dquarkus.native.container-build=true tells Quarkus to build inside a container (this one)
#RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true -DskipTests
#
#### Stage 2: Create the runtime image
## Use a minimal base image provided by Quarkus
#FROM quay.io/quarkus/quarkus-micro-image:1.0
#WORKDIR /work/
#
## Copy the native executable from the builder stage
#COPY --from=builder /code/target/*-runner /work/application
#
## Expose the port Quarkus listens on
#EXPOSE 8080
#
## Set user for runtime
#USER 1001
#
## Command to run the application
## -Dquarkus.http.host=0.0.0.0 makes Quarkus listen on all network interfaces inside the container
#CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]