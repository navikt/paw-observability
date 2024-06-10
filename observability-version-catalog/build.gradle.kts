plugins {
    `version-catalog`
    `maven-publish`
}

group = "no.nav.paw.observability"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
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


val openTelemetryInstrumentationVersion: String by project
val openTelemetrySdkTarget: String by project

catalog {
    versionCatalog {
        val ktorVersion = "2.3.11"
        val micrometerVersion = "1.12.3"
        version("ktor", ktorVersion)
        version("micrometer", micrometerVersion)
        version("openTelemetryInstrumentation", openTelemetryInstrumentationVersion)
        version("openTelemetrySdkTarget", openTelemetrySdkTarget)

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
        val ktorServerAuth = "ktor-server-auth"
        val ktorServerContentNegotiation = "ktorServerContentNegotiation"
        val ktorServerJackson = "ktorServerJackson"

        library(ktorServerCore, "$ktorGroup:ktor-server-core:$ktorVersion")
        library(ktorServerNetty, "$ktorGroup:ktor-server-netty:$ktorVersion")
        library(ktorServerCoreJvm, "$ktorGroup:ktor-server-core-jvm:$ktorVersion")

        library(ktorServerAuth, "$ktorGroup:ktor-server-auth:$ktorVersion")
        library(ktorServerContentNegotiation, "$ktorGroup:ktor-server-content-negotiation:$ktorVersion")
        library(ktorServerJackson, "$ktorGroup:ktor-serialization-jackson:$ktorVersion")

        library(ktorMetricsMicrometer, "$ktorGroup:ktor-server-metrics-micrometer:$ktorVersion")

        library(openTelemetryApi, "$openTelemetryGroup:opentelemetry-api:$openTelemetrySdkTarget")
        library(openTelemetryExporterOltp, "$openTelemetryGroup:opentelemetry-exporter-oltp:$openTelemetrySdkTarget")
        library(
            openTelemetryKtor,
            "${openTelemetryGroup}.instrumentation:opentelemetry-ktor-2.0:$openTelemetryInstrumentationVersion-alpha"
        )
        library(openTelemetrySemcov, "$openTelemetryGroup:opentelemetry-semcov:$openTelemetrySdkTarget")

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
