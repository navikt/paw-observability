plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.paw.observability"
version = "1.0"

repositories {
    mavenCentral()
}

val openTelemetryVersion: String by project
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifactId = "$artifactId-$openTelemetryVersion"
        }
    }
    repositories {
        maven {
            val mavenRepo: String by project
            setUrl("https://maven.pkg.github.com/navikt/$mavenRepo")
            credentials {
                username = "x-access-token"
                password = providers.environmentVariable("githubPassword").get()
            }
        }
    }
}

dependencies {
    compileOnly("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetryVersion")
    compileOnly("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetryVersion")
    compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetryVersion")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
    testImplementation("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetryVersion")
    testImplementation("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetryVersion")
    testImplementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetryVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
