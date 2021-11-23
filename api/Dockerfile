FROM alpine:latest

RUN apk update && apk add openjdk11 && apk add maven

COPY ./target/ /backend

WORKDIR /backend
EXPOSE 8080

CMD java -jar ./tlcdemoApp-1.0.0-SNAPSHOT-runner.jar
