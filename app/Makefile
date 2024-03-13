.PHONY: build frontend

clean:
	./gradlew clean

release:
	./gradlew clean build \
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
	./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew test jacocoTestReport

run:
	./gradlew bootRun

setup:
	./gradlew wrapper --gradle-version 8.5
	./gradlew build