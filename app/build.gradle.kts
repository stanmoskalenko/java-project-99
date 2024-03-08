import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.3"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
repositories.mavenCentral()
application.mainClass

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks {
    jacocoTestReport {
        reports.xml.required = true
    }
    test {
        useJUnitPlatform()
        reports.enabled
        testLogging.exceptionFormat = TestExceptionFormat.FULL
        testlogger {
            setTheme(ThemeType.MOCHA)
            showSummary = true
            showSkipped = true
            showFailed = true
            showPassed = true
        }
    }
}

dependencies {
    implementation("org.springframework:spring-webmvc:6.1.4")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.3.0")
    implementation("org.webjars:swagger-ui:4.8.1")

    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("net.datafaker:datafaker:2.1.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.postgresql:postgresql:42.7.0")

    // Test deps
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    testImplementation("org.springframework.security:spring-security-test:6.1.5")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
    testImplementation("org.instancio:instancio-junit:4.3.2")
    testImplementation("org.assertj:assertj-core:3.25.2")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}
