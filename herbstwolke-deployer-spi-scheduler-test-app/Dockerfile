FROM java:8-alpine

ARG JAR_FILE

ADD target/${JAR_FILE} herbstwolke-deployer-spi-scheduler-test-app.jar

ENTRYPOINT ["java", "-jar", "/herbstwolke-deployer-spi-scheduler-test-app.jar"]
