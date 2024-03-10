.PHONY: build frontend

setup:
	npm i @hexlet/java-task-manager-frontend; \
	gradle wrapper --gradle-version 8.5; \
	gradle build

clean:
	gradle clean

release:
	./gradlew clean bootJar \
	-Pprofile=prod \
    --no-daemon \
    --parallel \
    --exclude-task test \
    --exclude-task :checkstyleMain \
    --exclude-task :checkstyleTest \
    --exclude-task :check \
    --exclude-task :testClasses

build:
	./gradlew clean build

install:
	gradle installDist

lint:
	gradle checkstyleMain checkstyleTest

test:
	gradle test

report:
	gradle test jacocoTestReport

frontend:
	npx build-frontend