# Brokerless events

## Build project

```shell
mvn clean package
mvn jib:dockerBuild -pl brokerless-sample-application 
```

## Showcases

1. Application runs locally, in integration test and in docker with the same persistence configuration using Postgresql 
