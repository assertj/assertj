package org.example.custom.assertions.domain.assertions

import org.assertj.core.api.AbstractAssert
import org.example.custom.assertions.domain.DomainObject

class DomainObjectAssertion extends AbstractAssert<DomainObjectAssertion, DomainObject> {

    DomainObjectAssertion(DomainObject domainObject) {
        super(domainObject, DomainObjectAssertion)
    }

    DomainObjectAssertion hasName(String expectedName) {
        isNotNull()

        objects.assertEqual(writableAssertionInfo, actual.name, expectedName)

        this
    }

    DomainObjectAssertion hasValue(Integer expectedValue) {
        isNotNull()

        objects.assertEqual(writableAssertionInfo, actual.value, expectedValue)

        this
    }

}
