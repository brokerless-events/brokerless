# Brokerless events

## Build project

```shell
mvn clean package -DskipTests && d build -t brokerless-sample-application:1 .
mvn clean package -DskipTests -P v2 && d build -t brokerless-sample-application:2 .
mvn clean package -DskipTests -P v2 && d build -t brokerless-sample-application:3 .
```

## Showcases

1. Application runs locally, in integration test and in docker with the same persistence configuration using Postgresql 
