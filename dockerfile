FROM openjdk:11
COPY ${JAR_FILE} tech-test-jkbx.jar
ENTRYPOINT ["java", "-jar", "/tech-test-jkbx.jar"]