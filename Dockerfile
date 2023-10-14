FROM eclipse-temurin:17-jdk
COPY brokerless-sample-application/target/brokerless-sample-application-LATEST-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
