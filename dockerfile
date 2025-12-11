FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
COPY . .

# 👇 darle permisos de ejecución al mvnw
RUN chmod +x mvnw

# 👇 ahora sí se puede ejecutar
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /APP
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
