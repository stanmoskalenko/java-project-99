FROM node:14 AS FRONTEND

WORKDIR /usr/app

RUN npm i @hexlet/java-task-manager-frontend
RUN npx build-frontend

FROM gradle:8.5-jdk AS TEMP_BUILD_IMAGE

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY build.gradle.kts settings.gradle.kts $APP_HOME
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle app /home/gradle/wrapper
COPY app .

RUN gradle clean build \
    --no-daemon \
    --parallel \
    --exclude-task test \
    --exclude-task :checkstyleMain \
    --exclude-task :checkstyleTest \
    --exclude-task :check \
    --exclude-task :testClasses

FROM bellsoft/liberica-openjdk-alpine:17-cds
LABEL name=test-manager-99
ENV ARTIFACT_NAME="project-0.0.1-SNAPSHOT.jar"
ENV APP_HOME=/usr/app/

WORKDIR /usr/app/

COPY --from=FRONTEND $APP_HOME/src/main/resources/static ./static
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar -Dspring.profiles.active=prod ${ARTIFACT_NAME}
