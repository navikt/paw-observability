plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.paw.observability"

repositories {
    mavenCentral()
}

val openTelemetryInstrumentationVersion: String by project
val openTelemetrySdkTarget: String by project
val kotestVersion: String by project

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifactId = "$artifactId-$openTelemetryInstrumentationVersion"
        }
    }
    repositories {
        maven {
            val mavenRepo: String by project
            val githubPassword: String by project
            setUrl("https://maven.pkg.github.com/navikt/$mavenRepo")
            credentials {
                username = "x-access-token"
                password = githubPassword
            }
        }
    }
}

dependencies {
    compileOnly("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetrySdkTarget")
    compileOnly("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetrySdkTarget")
    compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetrySdkTarget")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.opentelemetry:opentelemetry-exporter-otlp:$openTelemetrySdkTarget")
    testImplementation("io.opentelemetry:opentelemetry-sdk-trace:$openTelemetrySdkTarget")
    testImplementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure-spi:$openTelemetrySdkTarget")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
