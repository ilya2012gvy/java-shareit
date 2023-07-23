FROM amazoncorretto:11

COPY target/*.jar app.jar
#ENV TZ=Moscow
ENTRYPOINT ["java","-jar","/app.jar"]