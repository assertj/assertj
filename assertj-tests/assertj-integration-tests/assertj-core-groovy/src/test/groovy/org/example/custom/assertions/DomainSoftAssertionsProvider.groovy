package org.example.custom.assertions

import org.assertj.core.api.StandardSoftAssertionsProvider
import org.example.custom.assertions.domain.DomainObject
import org.example.custom.assertions.domain.assertions.DomainObjectAssertion
import org.example.custom.assertions.domain.assertions.ListDomainObjectsAssertion

interface DomainSoftAssertionsProvider extends StandardSoftAssertionsProvider {

    default DomainObjectAssertion assertThat(DomainObject actual) {
        proxy(DomainObjectAssertion, DomainObject, actual)
    }

    default ListDomainObjectsAssertion assertThatDomainObjects(List<DomainObject> actual) {
        proxy(ListDomainObjectsAssertion, List, actual)
    }
}
