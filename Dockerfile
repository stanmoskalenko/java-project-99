FROM gradle:8.2-jdk AS TEMP_BUILD_IMAGE

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY build.gradle.kts settings.gradle.kts $APP_HOME
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/wrapper
COPY . .

RUN ./gradlew clean build  \
    --no-daemon  \
    --parallel \
    --exclude-task test  \
    --exclude-task :checkstyleMain  \
    --exclude-task :checkstyleTest \
    --exclude-task :check \
    --exclude-task :testClasses

FROM bellsoft/liberica-openjdk-debian:latest
LABEL name="test-manager-99"

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

ENV ARTIFACT_NAME=app-20-all.jar
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}