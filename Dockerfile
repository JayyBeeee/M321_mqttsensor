FROM eclipse-temurin:17

RUN mkdir /opt/app

COPY target/mqtt_sensor-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/app

CMD ["java", "--add-opens", "java.base/java.net=ALL-UNAMED","-jar", "/opt/app/mqtt_sensor-1.0-SNAPSHOT-jar-with-dependencies.jar"]