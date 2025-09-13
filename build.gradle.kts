object VERSIONS{
    const val LOMBOK_VERSION="1.18.40"
    const val MOCKITO_VERSION="5.19.0"
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok:${VERSIONS.LOMBOK_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.projectlombok:lombok:${VERSIONS.LOMBOK_VERSION}")

    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:${VERSIONS.MOCKITO_VERSION}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Mockito javaagent ve diğer JVM argümanları
    doFirst {
        val mockitoJar = configurations.testRuntimeClasspath.get()
            .files
            .find { it.name.startsWith("mockito-core") }

        if (mockitoJar != null) {
            jvmArgs(
                "-javaagent:${mockitoJar.absolutePath}",
                "-Xshare:off"
            )
        }
    }
}