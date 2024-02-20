package no.nav.opentelemetry.anonymisering

import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.common.AttributesBuilder
import java.util.function.BiConsumer

private val alwaysMaskAttributes = setOf(
    "messaging.kafka.message.key"
)

fun masker(verdier: Attributes): Attributes = MaskerteVerdier(verdier)

fun masker(nøkkel: AttributeKey<*>, verdi: Any): Pair<AttributeKey<*>, Any> {
    val maskertVerdi = masker(verdi)
    return when {
        maskertVerdi != verdi.toString() -> {
            AttributeKey.stringKey(nøkkel.key) to maskertVerdi
        }
        nøkkel.key in alwaysMaskAttributes -> {
            AttributeKey.stringKey(nøkkel.key) to "*"
        }
        else -> {
            nøkkel to verdi
        }
    }
}

private class MaskerteVerdier(kilde: Attributes) : Attributes {
    private val maskerteVerdier = kilde.asMap()
            .map { (nøkkel, verdi) ->
                masker(nøkkel, verdi)
            }.toMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(key: AttributeKey<T>): T? = maskerteVerdier[key] as T?

    override fun forEach(consumer: BiConsumer<in AttributeKey<*>, in Any>) {
        maskerteVerdier.forEach { (nokkel, verdi) ->
            consumer.accept(nokkel, verdi)
        }
    }

    override fun size(): Int = maskerteVerdier.size

    override fun isEmpty(): Boolean = maskerteVerdier.isEmpty()

    override fun asMap(): MutableMap<AttributeKey<*>, Any> = maskerteVerdier.toMutableMap()

    override fun toBuilder(): AttributesBuilder =
        Attributes.builder()
                .apply { putAll(this@MaskerteVerdier) }

}
