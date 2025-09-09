object VERSIONS{
    const val LOMBOK_VERSION="1.18.40"
}
plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.rodiugurlu"
version = "0.0.1-SNAPSHOT"
description = "JUnit Tests"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")// https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:${VERSIONS.LOMBOK_VERSION}")
    annotationProcessor("org.projectlombok:lombok:${VERSIONS.LOMBOK_VERSION}")

    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
