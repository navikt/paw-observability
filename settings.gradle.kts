plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "paw-observability"

include(
    "opentelemetry-anonymisering"
)
