plugins {
    kotlin("jvm")
    `java-library`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

val openTelemetryVersion = "1.29.0"

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.7")
    compileOnly("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetryVersion")
    compileOnly("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetryVersion")
    compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetryVersion")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
    testImplementation("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetryVersion")
    testImplementation("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetryVersion")
    testImplementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetryVersion")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
