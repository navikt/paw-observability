package no.nav.opentelemetry.anonymisering

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.trace.data.SpanData
import io.opentelemetry.sdk.trace.export.SpanExporter

class MaskerendeSpanExporter(private val parent: SpanExporter) : SpanExporter {
    override fun export(spans: MutableCollection<SpanData>): CompletableResultCode {
        val maskedSpans = spans.map(::maskSpan)
        return parent.export(maskedSpans)
    }

    private fun maskSpan(spanData: SpanData): SpanData =
            object: SpanData by spanData {
                override fun getAttributes(): Attributes = masker(spanData.attributes)
            }

    override fun flush(): CompletableResultCode = parent.flush()

    override fun shutdown(): CompletableResultCode = parent.flush()
}