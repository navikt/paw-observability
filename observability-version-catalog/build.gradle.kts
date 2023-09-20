plugins {
    `version-catalog`
    `maven-publish`
}

group = "no.nav.paw.observability"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    mavenNav("*")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
    repositories {
        mavenNav("paw-observability")
        mavenLocal()
    }
}



catalog {
    versionCatalog {
        val ktorVersion = "2.3.4"
        val micrometerVersion = "1.11.4"
        val openTelemetryVersion = "1.30.0"
        version("ktor", ktorVersion)
        version("micrometer", micrometerVersion)
        version("openTelemetry", openTelemetryVersion)

        val ktorGroup = "io.ktor"
        val micrometerGroup = "io.micrometer"
        val openTelemetryGroup = "io.opentelemetry"

        val ktorServerCore = "ktorServerCore"
        val ktorServerNetty = "ktorServerNetty"
        val ktorServerCoreJvm = "ktorServerCoreJvm"
        val ktorMetricsMicrometer = "ktorMetricsMicrometer"
        val openTelemetryApi = "openTelemetryApi"
        val openTelemetryExporterOltp = "openTelemetryExporterOltp"
        val openTelemetryKtor = "openTelemetryKtor"
        val openTelemetrySemcov = "openTelemetrySemcov"
        val micrometerRegistryPrometheus = "micrometerRegistryPrometheus"

        library(ktorServerCore, "$ktorGroup:ktor-server-core:$ktorVersion")
        library(ktorServerNetty, "$ktorGroup:ktor-server-netty:$ktorVersion")
        library(ktorServerCoreJvm, "$ktorGroup:ktor-server-core-jvm:$ktorVersion")
        library(ktorMetricsMicrometer, "$ktorGroup:ktor-server-metrics-micrometer:$ktorVersion")

        library(openTelemetryApi, "$openTelemetryGroup:opentelemetry-api:$openTelemetryVersion")
        library(openTelemetryExporterOltp, "$openTelemetryGroup:opentelemetry-exporter-oltp:$openTelemetryVersion")
        library(
            openTelemetryKtor,
            "${openTelemetryGroup}.instrumentation:opentelemetry-ktor-2.0:$openTelemetryVersion-alpha"
        )
        library(openTelemetrySemcov, "$openTelemetryGroup:opentelemetry-semcov:$openTelemetryVersion")

        library(micrometerRegistryPrometheus, "$micrometerGroup:micrometer-registry-prometheus:$micrometerVersion")

        bundle(
            "ktorNettyOpentelemetryMicrometerPrometheus",
            listOf(
                ktorServerCore,
                ktorServerCoreJvm,
                ktorServerNetty,
                ktorMetricsMicrometer,
                openTelemetryApi,
                openTelemetryKtor,
                micrometerRegistryPrometheus
            )
        )

    }
}

fun RepositoryHandler.mavenNav(repo: String): MavenArtifactRepository {
    val githubPassword: String by project

    return maven {
        setUrl("https://maven.pkg.github.com/navikt/$repo")
        credentials {
            username = "x-access-token"
            password = githubPassword
        }
    }
}
