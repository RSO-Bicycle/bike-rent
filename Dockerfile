FROM openjdk:8-jre-alpine
RUN mkdir /app
WORKDIR /app
ADD ./target/bike-rent-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "bike-rent-1.0.0-SNAPSHOT.jar"]