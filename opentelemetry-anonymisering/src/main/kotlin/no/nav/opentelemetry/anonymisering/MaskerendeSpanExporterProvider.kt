package no.nav.opentelemetry.anonymisering

import io.opentelemetry.exporter.otlp.internal.OtlpSpanExporterProvider
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSpanExporterProvider
import io.opentelemetry.sdk.trace.export.SpanExporter

class MaskerendeSpanExporterProvider: ConfigurableSpanExporterProvider {
    override fun createExporter(config: ConfigProperties): SpanExporter {
        val parent = OtlpSpanExporterProvider().createExporter(config)
        return MaskerendeSpanExporter(parent)
    }

    override fun getName(): String = "maskert_oltp"
}