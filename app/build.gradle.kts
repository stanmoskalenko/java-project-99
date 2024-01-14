import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.3"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
repositories.mavenCentral()
java.sourceCompatibility = JavaVersion.VERSION_20

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

application.mainClass

tasks.test {
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

tasks.jacocoTestReport {
    reports.xml.required = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    runtimeOnly("com.h2database:h2:2.2.220")
    runtimeOnly("org.postgresql:postgresql:42.5.4")

    // Test deps
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.1")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}
