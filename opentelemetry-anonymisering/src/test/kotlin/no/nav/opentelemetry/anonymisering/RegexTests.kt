package no.nav.opentelemetry.anonymisering

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RegexTests: StringSpec({
    "Tall med akkurat 11 siffer skal konverteres til '***********'" {
        masker("12345678901") shouldBe "***********"
        masker("etnummer12345678901") shouldBe "etnummer***********"
        masker("etnummer12345678901med") shouldBe "etnummer***********med"
        masker(" 12345678901") shouldBe " ***********"
        masker(" 12345678901 ") shouldBe " *********** "
        masker("12345678901 ") shouldBe "*********** "
        masker("12345678901\n") shouldBe "***********\n"
        masker(12345678901) shouldBe "***********"
    }

    "Tall med mer eller mindre enn 11 siffer skal ikke maskeres" {
        masker("012345678901") shouldBe "012345678901"
        masker(112345678901) shouldBe "112345678901"
        masker("123") shouldBe "123"
        masker("/123456789/") shouldBe "/123456789/"
        masker(1234) shouldBe "1234"
        masker(1234567890) shouldBe "1234567890"
        masker("1234567890") shouldBe "1234567890"
        masker(" 1234567890") shouldBe " 1234567890"
        masker("1234567890 ") shouldBe "1234567890 "
        masker(" 1234567890 ") shouldBe " 1234567890 "
        masker("a1234567890") shouldBe "a1234567890"
        masker("b1234567890c") shouldBe "b1234567890c"
    }
})