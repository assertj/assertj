package org.example.custom.assertions

import org.assertj.core.api.AbstractSoftAssertions
import org.assertj.core.api.SoftAssertionsProvider

import java.util.function.Consumer

class DomainSoftAssertions extends AbstractSoftAssertions implements DomainSoftAssertionsProvider {

    static void assertSoftly(Consumer<DomainSoftAssertions> softly) {
        SoftAssertionsProvider.assertSoftly(DomainSoftAssertions, softly)
    }

}
