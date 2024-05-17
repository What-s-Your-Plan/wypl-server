# Dockerfile
FROM amazoncorretto:17-alpine

LABEL MAINTAINER="Jiwon Han <gkswldnj0803@gmail.com>"

# JAR 파일을 컨테이너 내부로 복사
COPY ./build/libs/*.jar wypl.jar

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=${PROFILE}", "wypl.jar"]