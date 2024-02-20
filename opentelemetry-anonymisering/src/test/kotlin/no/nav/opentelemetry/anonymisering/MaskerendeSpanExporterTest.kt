package no.nav.opentelemetry.anonymisering

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.data.EventData
import io.opentelemetry.sdk.trace.data.LinkData
import io.opentelemetry.sdk.trace.data.SpanData
import io.opentelemetry.sdk.trace.data.StatusData
import io.opentelemetry.sdk.trace.export.SpanExporter
import org.junit.jupiter.api.fail

class MaskerendeSpanExporterTest: StringSpec({
    fun spanExporter(mottaker: (MutableCollection<SpanData>) -> Unit) =  object: SpanExporter {
        override fun export(spans: MutableCollection<SpanData>): CompletableResultCode {
            mottaker(spans)
            return CompletableResultCode.ofSuccess()
        }

        override fun flush(): CompletableResultCode = CompletableResultCode.ofSuccess()

        override fun shutdown(): CompletableResultCode = CompletableResultCode.ofSuccess()

    }

    "Span skal ikke inneholde 11 siffer" {
        val originalSpan = object : SpanData {
            override fun getName(): String = "navn"
            override fun getKind(): SpanKind = SpanKind.SERVER
            override fun getSpanContext(): SpanContext = SpanContext.getInvalid()
            override fun getParentSpanContext(): SpanContext = SpanContext.getInvalid()
            override fun getStatus(): StatusData = StatusData.ok()
            override fun getStartEpochNanos(): Long = System.nanoTime()
            override fun getAttributes(): Attributes = Attributes.of(
                AttributeKey.longKey("fødselsnummer"), 12345678901,
                AttributeKey.stringKey("no_annet"), "353214",
                AttributeKey.stringKey("messaging.kafka.message.key"), "1234"
            )
            override fun getEvents(): MutableList<EventData> = mutableListOf()
            override fun getLinks(): MutableList<LinkData> = mutableListOf()
            override fun getEndEpochNanos(): Long = System.nanoTime()
            override fun hasEnded(): Boolean = true
            override fun getTotalRecordedEvents(): Int = 2
            override fun getTotalRecordedLinks(): Int = 0
            override fun getTotalAttributeCount(): Int = 1
            @Deprecated("Underliggende klasser er deprikert")
            override fun getInstrumentationLibraryInfo(): InstrumentationLibraryInfo = InstrumentationLibraryInfo.empty()
            override fun getResource(): Resource = Resource.empty()
        }
        val spans = mutableListOf<SpanData>()
        val exporter = MaskerendeSpanExporter(spanExporter { spanData -> spans.addAll(spanData) })
        exporter.export(mutableListOf(originalSpan))
        spans.size shouldBe 1
        spans.forEach{ spanData ->
            spanData.attributes.size() shouldBe 3
            spanData.attributes.forEach{ nøkkel, verdi ->
                when (nøkkel.key) {
                    "fødselsnummer" -> verdi shouldBe "***********"
                    "no_annet" -> verdi shouldBe "353214"
                    "messaging.kafka.message.key" -> verdi shouldBe "*"
                    else -> fail { "uventet nøkkel: $nøkkel" }
                }
            }
        }
    }
})