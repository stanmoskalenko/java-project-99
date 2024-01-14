.PHONY: build

gradle = ./app/.gradlew

setup:
	gradle wrapper --gradle-version 8.5
	gradle build

clean:
	gradle clean

build:
	gradle clean build

install:
	gradle installDist

lint:
	gradle checkstyleMain checkstyleTest

test:
	gradle test

report:
	gradle test jacocoTestReport
